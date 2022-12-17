package dev.marfien.servicediscovery.registry.service

import dev.marfien.servicediscovery.registry.model.Service
import dev.marfien.servicediscovery.registry.model.TopicGroup
import dev.marfien.servicediscovery.registry.repository.ServiceRepository
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import org.springframework.stereotype.Service as SpringService

interface ServiceService {

    fun findAll(): Flux<Service>
    fun findAll(pageable: Pageable): Flux<Service>

    fun findAllByHost(host: String): Flux<Service>
    fun findAllByTopic(topic: String): Flux<Service>

    fun findById(id: String): Mono<Service>
    fun findAllSortedByTopic(): Flux<TopicGroup>

    fun save(service: Service): Mono<Service>
    fun remove(id: String): Mono<Void?>


}

@SpringService
class ServiceServiceImpl(val repository: ServiceRepository) : ServiceService {

    override fun findAll(): Flux<Service> = this.repository.findAll()

    override fun findAll(pageable: Pageable): Flux<Service> = this.repository.findAll(pageable)

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