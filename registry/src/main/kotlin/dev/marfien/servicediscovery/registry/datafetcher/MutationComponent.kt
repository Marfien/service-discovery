package dev.marfien.servicediscovery.registry.datafetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import dev.marfien.servicediscovery.model.ServiceInput
import dev.marfien.servicediscovery.model.ServiceType
import dev.marfien.servicediscovery.registry.service.ServiceService
import reactor.core.publisher.Mono

@DgsComponent
class MutationComponent(private val serviceService: ServiceService) {

    @DgsMutation
    fun registerService(@InputArgument service: ServiceInput): Mono<ServiceType> = this.serviceService.save(service)

    @DgsMutation
    fun removeService(@InputArgument id: String): Mono<Void?> = this.serviceService.remove(id)

    @DgsMutation
    fun updateTTL(@InputArgument id: String): Mono<Boolean> = this.serviceService.updateTTL(id)

}