package dev.marfien.servicediscovery.registry.service

import dev.marfien.servicediscovery.model.*
import dev.marfien.servicediscovery.registry.repository.ServiceRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*
import org.springframework.stereotype.Service as SpringService

interface ServiceService {

    fun findAll(pagination: Pagination?): Flux<RegisteredService>

    fun findAllByHost(host: String): Flux<RegisteredService>
    fun findAllByTopic(topic: String): Flux<RegisteredService>

    fun findById(id: String): Mono<RegisteredService>
    fun findAllSortedByTopic(): Flux<TopicGroup>

    fun save(service: Service): Mono<RegisteredService>
    fun remove(id: String): Mono<Void?>
    fun updateTTL(id: String): Mono<Boolean>


}

@SpringService
class ServiceServiceImpl(val repository: ServiceRepository) : ServiceService {


    override fun findAll(pagination: Pagination?): Flux<RegisteredService> =
        if (pagination == null) this.repository.findAll()
        else this.repository.findAll(PageRequest.of(pagination.page, pagination.pageSize))

    override fun findAllByHost(host: String): Flux<RegisteredService> = this.repository.findAllByHost(host)

    override fun findAllByTopic(topic: String): Flux<RegisteredService> = this.repository.findAllByTopic(topic)

    override fun findById(id: String): Mono<RegisteredService> = this.repository.findById(id)

    override fun findAllSortedByTopic(): Flux<TopicGroup> {
        return this.repository.findAll()
            .collectList()
            .map { list -> list.groupBy { it.topic }.map { TopicGroup(it.key, it.value) } }
            .flatMapIterable { it }
    }

    override fun save(service: Service): Mono<RegisteredService> = this.repository.save(when (service) {
        is RegisteredService -> service
        is AnonymousService -> service.toRegisteredService()
        else -> throw NotImplementedError("Service type not proper implemented")
    })

    override fun remove(id: String): Mono<Void?> = this.repository.deleteById(id)

    override fun updateTTL(id: String): Mono<Boolean> = this.repository.existsById(id).doOnNext { exists ->
        if (!exists) return@doOnNext

        this.findById(id).doOnNext {
            it.lastHealthSignal = Date()
            this.save(it)
        }
    }

}