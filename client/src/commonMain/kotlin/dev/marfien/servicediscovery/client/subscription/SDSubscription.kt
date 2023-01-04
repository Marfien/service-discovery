package dev.marfien.servicediscovery.client.subscription

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.Subscription
import com.benasher44.uuid.Uuid
import dev.marfien.servicediscovery.client.OperationType
import dev.marfien.servicediscovery.client.SDOperation
import dev.marfien.servicediscovery.client.model.ReturnTypeBuilder
import dev.marfien.servicediscovery.client.model.ServiceEventReturnTypeBuilder
import dev.marfien.servicediscovery.json.JsonAdapter
import dev.marfien.servicediscovery.json.JsonReader
import dev.marfien.servicediscovery.model.ListInput
import dev.marfien.servicediscovery.model.ServiceEvent
import dev.marfien.servicediscovery.model.ServiceEventType
import kotlin.random.Random

abstract class SDSubscription<D : Subscription.Data>(
//    val variables: Map<String, InputType> = mapOf(),
    builder: ReturnTypeBuilder
) : SDOperation(OperationType.SUBSCRIPTION, builder), Subscription<D> {

    companion object {

        fun create(block: CustomSDSubscriptionBuilder.() -> Unit) = CustomSDSubscription(CustomSDSubscriptionBuilder().apply(block))

    }

}

class CustomSDSubscription internal constructor(
    builder: CustomSDSubscriptionBuilder
) : SDSubscription<CustomSDSubscriptionData>(builder) {

    override fun adapter(): Adapter<CustomSDSubscriptionData> = CustomSDSubscriptionDataAdapter

    override fun id(): String = id.toString()

    companion object {

        val id = Uuid(Random.nextLong(), Random.nextLong())

    }

}

object CustomSDSubscriptionDataAdapter : JsonAdapter<CustomSDSubscriptionData> {

    override fun fromJson(reader: JsonReader): CustomSDSubscriptionData {
        val data = CustomSDSubscriptionData()

        while (reader.hasNext()) {
            when (val name = reader.nextName()) {
                "listenTo" -> data.listenTo = ListenToDataAdapter.fromJson(reader).event
                else -> {
                    println("Skipping unknown value: $name")
                    reader.skipValue()
                }
            }
        }

        return data
    }
}

data class CustomSDSubscriptionData(
    var listenTo: ServiceEvent? = null,
) : Subscription.Data

class CustomSDSubscriptionBuilder internal constructor() : ReturnTypeBuilder() {

    fun listenTo(types: List<ServiceEventType>, block: ServiceEventReturnTypeBuilder.() -> Unit) = apply {
        val builder = ServiceEventReturnTypeBuilder().apply(block)

        withField("listenTo", builder, "event" to ListInput(types))
    }

}