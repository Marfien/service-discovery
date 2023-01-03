package dev.marfien.servicediscovery.client.mutation

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.Mutation
import com.benasher44.uuid.Uuid
import dev.marfien.servicediscovery.client.OperationType
import dev.marfien.servicediscovery.client.SDOperation
import dev.marfien.servicediscovery.client.model.ReturnTypeBuilder
import dev.marfien.servicediscovery.client.model.ServiceReturnTypeBuilder
import dev.marfien.servicediscovery.json.JsonAdapter
import dev.marfien.servicediscovery.json.JsonReader
import dev.marfien.servicediscovery.model.ServiceInput
import dev.marfien.servicediscovery.model.ServiceType
import dev.marfien.servicediscovery.model.StringInput
import kotlin.random.Random

abstract class SDMutation<D : Mutation.Data>(
//    val variables: Map<String, InputType> = mapOf(),
    builder: ReturnTypeBuilder
) : SDOperation(OperationType.MUTATION, builder), Mutation<D> {

    companion object {

        fun create(block: CustomSDMutationBuilder.() -> Unit) = CustomSDMutation(CustomSDMutationBuilder().apply(block))

    }

}

class CustomSDMutation internal constructor(
    builder: CustomSDMutationBuilder
) : SDMutation<CustomSDMutationData>(builder) {

    override fun adapter(): Adapter<CustomSDMutationData> = CustomSDMutationDataAdapter

    override fun id(): String = id.toString()

    companion object {

        val id = Uuid(Random.nextLong(), Random.nextLong())

    }

}

object CustomSDMutationDataAdapter : JsonAdapter<CustomSDMutationData> {

    override fun fromJson(reader: JsonReader): CustomSDMutationData {
        val data = CustomSDMutationData()

        while (reader.hasNext()) {
            when (val name = reader.nextName()) {
                "registerService" -> data.registerService = RegisterServiceDataAdapter.fromJson(reader).service
                "removeService" -> data.removeService = RemoveServiceDataAdapter.fromJson(reader).existed
                "updateTTL" -> data.updateTTL = UpdateTtlDataAdapter.fromJson(reader).exists
                else -> {
                    println("Skipping unknown value: $name")
                    reader.skipValue()
                }
            }
        }

        return data
    }
}

data class CustomSDMutationData(
    var registerService: ServiceType? = null,
    var removeService: Boolean? = null,
    var updateTTL: Boolean? = null,
) : Mutation.Data

class CustomSDMutationBuilder internal constructor() : ReturnTypeBuilder() {

    fun registerService(service: ServiceInput, block: ServiceReturnTypeBuilder.() -> Unit) = apply {
        val builder = ServiceReturnTypeBuilder().apply(block)

        withField("registerService", builder, "service" to service)
    }

    fun removeService(id: String) = apply {
        withField("removeService", "id" to StringInput(id))
    }

    fun updateTTL(id: String) = apply {
        withField("updateTTL", "id" to StringInput(id))
    }

}