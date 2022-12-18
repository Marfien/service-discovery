package dev.marfien.servicediscovery.registry.subscription

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.server.operations.Subscription
import dev.marfien.servicediscovery.registry.model.Service
import org.reactivestreams.Publisher
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink

val registrationSubscribers = mutableListOf<FluxSink<Service>>()
val removalSubscribers = mutableListOf<FluxSink<Service>>()

@GraphQLIgnore
@Controller
class SubscriptionController(val template: ReactiveMongoTemplate) {

    init {
        this.subscribeRegister()
            .subscribe { service -> registrationSubscribers.forEach { it.next(service) } }
        this.subscribeRemove()
            .subscribe { service -> removalSubscribers.forEach { it.next(service) } }
    }

    fun subscribeRegister(): Flux<Service> =
        this.template
            .changeStream(Service::class.java)
            .watchCollection("services")
            .filter(Criteria.where("operationType").`in`("insert"))
            .listen()
            .mapNotNull { it.body }

    fun subscribeRemove(): Flux<Service> =
        this.template
            .changeStream(Service::class.java)
            .watchCollection("services")
            .filter(Criteria.where("operationType").`in`("delete"))
            .listen()
            .mapNotNull { it.body }

}

@Component
class ServiceSubscription(private val template: ReactiveMongoTemplate) : Subscription {

    @GraphQLDescription("""
        Subscribes to all ge
    """)
    fun listenToRegistrations(): Publisher<Service> =
        Flux.create({
            registrationSubscribers.add( it.onDispose { registrationSubscribers.remove(it) } )
        }, FluxSink.OverflowStrategy.LATEST)

    fun listenToRemoval(): Publisher<Service> =
        Flux.create({
            removalSubscribers.add( it.onDispose { removalSubscribers.remove(it) } )
        }, FluxSink.OverflowStrategy.LATEST)

}