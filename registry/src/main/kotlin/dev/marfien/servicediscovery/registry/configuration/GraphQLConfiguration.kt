package dev.marfien.servicediscovery.registry.configuration

import dev.marfien.servicediscovery.registry.model.Service
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.FluxSink

@Configuration
class GraphQLConfiguration {

    @Bean(name = ["registerSubscribers"])
    fun registerSinks(): MutableCollection<FluxSink<Service>> = mutableListOf()

    @Bean(name = ["removeSubscribers"])
    fun removeSinks(): MutableCollection<FluxSink<Service>> = mutableListOf()

}