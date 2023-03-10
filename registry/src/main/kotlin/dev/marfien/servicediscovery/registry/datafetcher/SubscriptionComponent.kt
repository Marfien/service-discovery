package dev.marfien.servicediscovery.registry.datafetcher

import com.mongodb.client.model.changestream.OperationType
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsSubscription
import com.netflix.graphql.dgs.InputArgument
import dev.marfien.servicediscovery.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import java.util.logging.Logger
import javax.annotation.PostConstruct

@DgsComponent
class SubscriptionComponent(
    private val mongoSubscriber: MongoSubscriber
) {

    @DgsSubscription
    fun listenTo(@InputArgument event: Collection<ServiceEventType>): Flux<ServiceEvent> = Flux.create({ sink ->
        val lists = event.map { this.mongoSubscriber.subscribers[it]!! }.onEach { it.add(sink) }
        sink.onDispose { lists.forEach { it.remove(sink) } }
    }, FluxSink.OverflowStrategy.LATEST)

}

@Component
class MongoSubscriber {

    val subscribers = mutableMapOf<ServiceEventType, MutableCollection<FluxSink<ServiceEvent>>>(
        ServiceEventType.REGISTRATION to mutableListOf(),
        ServiceEventType.UPDATE to mutableListOf(),
        ServiceEventType.REMOVAL to mutableListOf()
    )

    @Autowired
    private lateinit var template: ReactiveMongoTemplate

    @PostConstruct
    fun init() {
        this.subscribeToMongo().subscribe {
            this.subscribers[it.type]!!.forEach { sink -> sink.next(it) }
        }
    }

    fun subscribeToMongo(): Flux<ServiceEvent> = this.template
        .changeStream(RegisteredService::class.java)
        .watchCollection("services")
        .filter(Criteria.where("operationType").`in`("delete", "insert", "update"))
        .listen()
        // TODO body on delete event
        .mapNotNull {
            ServiceEvent(
                it.operationType!!.toServiceEventType(),
                it.body ?: RegisteredService("Not implemented", Network("Not implemented", -1), "Not implemented")
            )
        }

}

fun OperationType.toServiceEventType(): ServiceEventType = when (this) {
    OperationType.INSERT -> ServiceEventType.REGISTRATION
    OperationType.UPDATE -> ServiceEventType.UPDATE
    OperationType.DELETE -> ServiceEventType.REMOVAL
    else -> throw NotImplementedError("Unsupported OperationType: $this")
}