package dev.marfien.servicediscovery.registry.schema

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.Schema
import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import dev.marfien.servicediscovery.registry.model.AnonymousService
import dev.marfien.servicediscovery.registry.model.Service
import dev.marfien.servicediscovery.registry.model.TopicGroup
import dev.marfien.servicediscovery.registry.service.ServiceService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@GraphQLDescription("Service schema")
@Component
class SchemaComponent : Schema

@Component
class ServiceQuery(private val service: ServiceService) : Query {

    fun findServiceById(id: ID): Mono<Service> = this.service.findById(id.toString())

    fun findAllServices(): Flux<Service> = this.service.findAll()

    fun findAllServicesInPage(page: Int, size: Int): Flux<Service> = this.service.findAll(PageRequest.of(page, size))

    fun findAllServicesByHost(host: String): Flux<Service> = this.service.findAllByHost(host)

    fun findAllServicesByTopic(topic: String): Flux<Service> = this.service.findAllByTopic(topic)

    fun findAllServicesSortedByTopic(): Flux<TopicGroup> = this.service.findAllSortedByTopic()

}

@Component
class ServiceMutation(private val service: ServiceService) : Mutation {

    fun registerService(service: AnonymousService): Mono<Service> = this.service.save(service.asService())

    fun unregisterService(id: ID): Mono<Void?> = this.service.remove(id.toString()).map { null }

}