package dev.marfien.servicediscovery.registry.datafetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import dev.marfien.servicediscovery.model.AnonymousService
import dev.marfien.servicediscovery.model.RegisteredService
import dev.marfien.servicediscovery.registry.service.ServiceService
import reactor.core.publisher.Mono

@DgsComponent
class MutationComponent(private val serviceService: ServiceService) {

    @DgsMutation
    fun register(@InputArgument service: AnonymousService): Mono<RegisteredService> = this.serviceService.save(service)

    @DgsMutation
    fun remove(@InputArgument id: String): Mono<Void?> = this.serviceService.remove(id)

    @DgsMutation
    fun updateTTL(@InputArgument id: String): Mono<Boolean> = this.serviceService.updateTTL(id)

}