package dev.marfien.servicediscovery.registry.service

import dev.marfien.servicediscovery.model.*
import dev.marfien.servicediscovery.registry.model.WrappedServiceType
import dev.marfien.servicediscovery.registry.model.toWrappedService
import dev.marfien.servicediscovery.registry.repository.ServiceRepository
import org.springframework.data.domain.PageRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*
import org.springframework.stereotype.Service as SpringService

interface ServiceService {

    fun findAll(pagination: PaginationInput?): Flux<ServiceType>

    fun findAllByHost(host: String): Flux<ServiceType>
    fun findAllByTopic(topic: String): Flux<ServiceType>

    fun findById(id: String): Mono<ServiceType>
    fun findAllSortedByTopic(): Flux<TopicGroup>

    fun save(service: ServiceInput): Mono<ServiceType>
    fun remove(id: String): Mono<Void?>
    fun updateTTL(id: String): Mono<Boolean>


}

@SpringService
class ServiceServiceImpl(val repository: ServiceRepository) : ServiceService {


    override fun findAll(pagination: PaginationInput?): Flux<ServiceType> = mapFlux {
        if (pagination == null) this.repository.findAll()
        else this.repository.findAll(PageRequest.of(pagination.page, pagination.pageSize))
    }


    override fun findAllByHost(host: String): Flux<ServiceType> = mapFlux {
        this.repository.findAllByHost(host)
    }

    override fun findAllByTopic(topic: String): Flux<ServiceType> = mapFlux {
        this.repository.findAllByTopic(topic)
    }

    override fun findById(id: String): Mono<ServiceType> = mapMono {
        this.repository.findById(id)
    }

    override fun findAllSortedByTopic(): Flux<TopicGroup> {
        return this.repository.findAll()
            .collectList()
            .map { list ->
                list.groupBy { it.topic }
                    .map { TopicGroup(it.key, it.value.map { wrappedServiceType ->  wrappedServiceType.toServiceType() }) } }
            .flatMapIterable { it }
    }

    override fun save(service: ServiceInput): Mono<ServiceType> = mapMono {
        this.repository.save(service.toWrappedService())
    }

    override fun remove(id: String): Mono<Void?> = this.repository.deleteById(id)

    override fun updateTTL(id: String): Mono<Boolean> = this.repository.existsById(id).doOnNext { exists ->
        if (!exists) return@doOnNext

        this.repository.findById(id).doOnNext {
            it.lastHealthSignal = Date()
            this.repository.save(it)
        }
    }

    fun mapMono(producer: () -> Mono<WrappedServiceType>) = producer().map { it.toServiceType() }

    fun mapFlux(producer: () -> Flux<WrappedServiceType>) = producer().map { it.toServiceType() }

}