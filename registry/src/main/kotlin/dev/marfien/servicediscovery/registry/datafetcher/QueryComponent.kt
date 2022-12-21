package dev.marfien.servicediscovery.registry.datafetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import dev.marfien.servicediscovery.model.Service
import dev.marfien.servicediscovery.registry.service.ServiceService
import reactor.core.publisher.Flux

@DgsComponent
class QueryComponent(private val serviceService: ServiceService) {

    @DgsQuery
    fun findAllServices(): Flux<Service> = this.serviceService.findAll()

}