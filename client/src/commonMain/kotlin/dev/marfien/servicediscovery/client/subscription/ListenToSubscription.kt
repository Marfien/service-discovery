package dev.marfien.servicediscovery.client.subscription

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.Subscription
import com.benasher44.uuid.Uuid
import dev.marfien.servicediscovery.client.model.ServiceEventReturnTypeBuilder
import dev.marfien.servicediscovery.json.JsonAdapter
import dev.marfien.servicediscovery.json.JsonReader
import dev.marfien.servicediscovery.model.ServiceEvent
import dev.marfien.servicediscovery.model.ServiceEventType
import kotlin.random.Random

class ListenToSubscription(
    vararg events: ServiceEventType,
    block: ServiceEventReturnTypeBuilder.() -> Unit
) : SDSubscription<ListenToData>(
    CustomSDSubscriptionBuilder().listenTo(events.toList(), block)
) {

    override fun adapter(): Adapter<ListenToData> = ListenToDataAdapter

    override fun id(): String = id.toString()

    companion object {

        val id = Uuid(Random.nextLong(), Random.nextLong())

    }

}

data class ListenToData(
    val event: ServiceEvent
) : Subscription.Data

object ListenToDataAdapter : JsonAdapter<ListenToData> {

    override fun fromJson(reader: JsonReader): ListenToData = ListenToData(
        ServiceEvent.fromJson(reader)
    )
}