package dev.marfien.servicediscovery.registry.service

import dev.marfien.servicediscovery.model.Pagination
import dev.marfien.servicediscovery.model.Service
import dev.marfien.servicediscovery.model.TopicGroup
import dev.marfien.servicediscovery.registry.repository.ServiceRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import org.springframework.stereotype.Service as SpringService

interface ServiceService {

    fun findAll(pagination: Pagination?): Flux<Service>

    fun findAllByHost(host: String): Flux<Service>
    fun findAllByTopic(topic: String): Flux<Service>

    fun findById(id: String): Mono<Service>
    fun findAllSortedByTopic(): Flux<TopicGroup>

    fun save(service: Service): Mono<Service>
    fun remove(id: String): Mono<Void?>


}

@SpringService
class ServiceServiceImpl(val repository: ServiceRepository) : ServiceService {


    override fun findAll(pagination: Pagination?): Flux<Service> =
        if (pagination == null) this.repository.findAll()
        else this.repository.findAll(PageRequest.of(pagination.page, pagination.pageSize))

    override fun findAllByHost(host: String): Flux<Service> = this.repository.findAllByHost(host)

    override fun findAllByTopic(topic: String): Flux<Service> = this.repository.findAllByTopic(topic)

    override fun findById(id: String): Mono<Service> = this.repository.findById(id)

    override fun findAllSortedByTopic(): Flux<TopicGroup> {
        return this.repository.findAll()
            .collectList()
            .map { list -> list.groupBy { it.topic }.map { TopicGroup(it.key, it.value) } }
            .flatMapIterable { it }
    }

    override fun save(service: Service): Mono<Service> = this.repository.save(service)

    override fun remove(id: String): Mono<Void?> = this.repository.deleteById(id)
}