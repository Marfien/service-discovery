package dev.marfien.servicediscovery.registry.repository

import dev.marfien.servicediscovery.model.RegisteredService
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface ServiceRepository : ReactiveMongoRepository<RegisteredService, String> {

    @Query("""
        {
          'network.host': ?0
        }
    """)
    fun findAllByHost(hostname: String): Flux<RegisteredService>

    fun findAllByTopic(topic: String): Flux<RegisteredService>

    @Query("{ id: { \$exists: true }}")
    fun findAll(pageable: Pageable): Flux<RegisteredService>

}