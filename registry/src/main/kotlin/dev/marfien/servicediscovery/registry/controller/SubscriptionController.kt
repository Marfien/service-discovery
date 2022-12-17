package dev.marfien.servicediscovery.registry.controller

import dev.marfien.servicediscovery.registry.component.SubscriptionComponent
import dev.marfien.servicediscovery.registry.model.Service
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.graphql.data.method.annotation.SubscriptionMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.FluxSink.OverflowStrategy

@Controller
class SubscriptionController(
    @Qualifier("registerSubscribers") val registerSubscribers: MutableCollection<FluxSink<Service>>,
    @Qualifier("removeSubscribers") val removeSubscribers: MutableCollection<FluxSink<Service>>,
    val component: SubscriptionComponent
) {

    init {
        this.component.subscribeRegister()
            .subscribe { this.registerSubscribers.forEach { sub -> sub.next(it) } }
        this.component.subscribeRemove()
            .subscribe { this.registerSubscribers.forEach { sub -> sub.next(it) } }
    }

    @SubscriptionMapping
    fun serviceRegistered(): Publisher<Service> {
        println("sub register")
        return Flux.create({
            println("added")
            this.registerSubscribers.add(it.onDispose { this.registerSubscribers.remove(it); println("removed") })
        }, OverflowStrategy.LATEST).doOnError { println(it) }
    }

    @SubscriptionMapping
    fun serviceRemoved(): Publisher<Service> =
        Flux.create({
            this.removeSubscribers.add(it.onDispose { this.removeSubscribers.remove(it) })
        }, OverflowStrategy.LATEST)

}