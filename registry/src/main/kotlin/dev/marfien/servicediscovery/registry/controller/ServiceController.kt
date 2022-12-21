package dev.marfien.servicediscovery.registry.controller
//
//import dev.marfien.servicediscovery.registry.model.AnonymousService
//import dev.marfien.servicediscovery.registry.model.Service
//import dev.marfien.servicediscovery.registry.model.TopicGroup
//import dev.marfien.servicediscovery.registry.service.ServiceService
//import graphql.GraphQLContext
//import org.springframework.data.domain.PageRequest
//import org.springframework.graphql.data.method.annotation.Argument
//import org.springframework.graphql.data.method.annotation.MutationMapping
//import org.springframework.graphql.data.method.annotation.QueryMapping
//import org.springframework.stereotype.Controller
//import reactor.core.publisher.Flux
//import reactor.core.publisher.Mono
//import java.lang.Exception
//
//@Controller
//class ServiceController(val serviceService: ServiceService) {
//
//    @QueryMapping
//    fun findAllServicesPaged(@Argument page: Int = 0, @Argument pageSize: Int = 9): Flux<Service> =
//        this.serviceService.findAll(PageRequest.of(page, pageSize))
//
//    @QueryMapping
//    fun findAllServices(): Flux<Service> = this.serviceService.findAll()
//
//    @QueryMapping
//    fun findServiceById(@Argument id: String): Mono<Service> = this.serviceService.findById(id)
//
//    @QueryMapping
//    fun findAllServicesByHost(@Argument host: String): Flux<Service> = this.serviceService.findAllByHost(host)
//
//    @QueryMapping
//    fun findAllServicesByTopic(@Argument topic: String): Flux<Service> = this.serviceService.findAllByTopic(topic)
//
//    @QueryMapping
//    fun findAllSortedByTopic(): Flux<TopicGroup> = this.serviceService.findAllSortedByTopic()
//
//    @MutationMapping
//    fun register(@Argument service: AnonymousService, ctx: GraphQLContext): Mono<Service> = this.serviceService.save(service.asService())
//
//    @MutationMapping
//    fun remove(@Argument id: String): Mono<Boolean> = this.serviceService.remove(id).map { it == null }
//
//}