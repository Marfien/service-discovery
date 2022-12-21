package dev.marfien.servicediscovery.registry.component

import dev.marfien.servicediscovery.model.Service
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class SubscriptionComponent(val template: ReactiveMongoTemplate) {

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