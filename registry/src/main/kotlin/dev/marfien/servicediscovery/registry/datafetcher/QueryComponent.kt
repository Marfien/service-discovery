package dev.marfien.servicediscovery.registry.datafetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import dev.marfien.servicediscovery.model.PaginationInput
import dev.marfien.servicediscovery.model.ServiceType
import dev.marfien.servicediscovery.model.TopicGroup
import dev.marfien.servicediscovery.registry.service.ServiceService
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@DgsComponent
class QueryComponent(private val serviceService: ServiceService) {

    @DgsQuery
    fun findAllServices(@InputArgument pagination: PaginationInput?): Flux<ServiceType> = this.serviceService.findAll(pagination)

    @DgsQuery
    fun findAllServicesByHost(@InputArgument host: String): Flux<ServiceType> = this.serviceService.findAllByHost(host)

    @DgsQuery
    fun findAllServicesByTopic(@InputArgument topic: String): Flux<ServiceType> = this.serviceService.findAllByTopic(topic)

    @DgsQuery
    fun findAllServicesSortedByTopic(): Flux<TopicGroup> = this.serviceService.findAllSortedByTopic()

    @DgsQuery
    fun findServiceById(@InputArgument id: String): Mono<ServiceType> = this.serviceService.findById(id)

}