package dev.marfien.servicediscovery.client.query

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.CompiledField
import com.apollographql.apollo3.api.CustomScalarAdapters
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.api.json.JsonReader
import com.apollographql.apollo3.api.json.JsonWriter
import com.benasher44.uuid.Uuid
import dev.marfien.servicediscovery.client.model.ReturnTypeBuilder
import dev.marfien.servicediscovery.client.model.ServiceReturnTypeBuilder
import dev.marfien.servicediscovery.client.model.TopicGroupReturnTypeBuilder
import dev.marfien.servicediscovery.json.JsonReaderBridge
import dev.marfien.servicediscovery.model.PaginationInput
import dev.marfien.servicediscovery.model.ServiceType
import dev.marfien.servicediscovery.model.StringInput
import dev.marfien.servicediscovery.model.TopicGroup
import kotlin.random.Random

abstract class SDQuery<D : Query.Data>(
//    val variables: Map<String, InputType> = mapOf(),
    private val builder: ReturnTypeBuilder
) : Query<D> {

    override fun name(): String = this::class.simpleName ?: "AnonymousSDQuery"

    override fun document(): String =
        "query ${this.name()} ${
            this.builder.toDocument()
        }"

    override fun serializeVariables(writer: JsonWriter, customScalarAdapters: CustomScalarAdapters) = Unit //this.variables.takeUnless { it.isEmpty() }?.let {
//        val bridge = JsonWriterBridge(writer)
//
//        it.forEach { (name, value) -> value.writeJson(bridge.name(name)) }
//
//    } ?: Unit

    companion object {

        fun create(block: CustomQueryBuilder.() -> Unit) = CustomSDQuery(CustomQueryBuilder().apply(block))

    }

}

class CustomSDQuery internal constructor(
    builder: CustomQueryBuilder
) : SDQuery<CustomSDQueryData>(builder) {

    override fun adapter(): Adapter<CustomSDQueryData> = CustomSDQueryDataAdapter

    override fun id(): String = id.toString()

    override fun rootField(): CompiledField {
        TODO("Not yet implemented")
    }

    companion object {

        val id = Uuid(Random.nextLong(), Random.nextLong())

    }

}

object CustomSDQueryDataAdapter : Adapter<CustomSDQueryData> {

    override fun fromJson(reader: JsonReader, customScalarAdapters: CustomScalarAdapters): CustomSDQueryData {
        val bridge = JsonReaderBridge(reader)
        val data = CustomSDQueryData()

        while (bridge.hasNext()) {
            when (val name = bridge.nextName()) {
                "findAllServices" -> data.findAllServices = mutableListOf<ServiceType>().apply { bridge.nextArray { this += ServiceType.fromJson(bridge) } }
                "findAllServicesByHost" -> data.findAllServicesByHost = mutableListOf<ServiceType>().apply { bridge.nextArray { this += ServiceType.fromJson(bridge) } }
                "findAllServicesBtTopic" -> data.findAllServicesByTopic = mutableListOf<ServiceType>().apply { bridge.nextArray { this += ServiceType.fromJson(bridge) } }
                "findAllServicesSortedByTopic" -> data.findAllServicesSortedByTopic = mutableListOf<TopicGroup>().apply { bridge.nextArray { this += TopicGroup.fromJson(bridge) } }
                "findServicesById" -> data.findServiceById = ServiceType.fromJson(bridge)
                else -> {
                    println("Skipping value: $name")
                    reader.skipValue()
                }
            }
        }

        return data
    }

    override fun toJson(writer: JsonWriter, customScalarAdapters: CustomScalarAdapters, value: CustomSDQueryData) {
        TODO("Not yet implemented")
    }
}

data class CustomSDQueryData(
    var findAllServices: List<ServiceType> = listOf(),
    var findAllServicesByHost: List<ServiceType> = listOf(),
    var findAllServicesByTopic: List<ServiceType> = listOf(),
    var findAllServicesSortedByTopic: List<TopicGroup> = listOf(),
    var findServiceById: ServiceType? = null
) : Query.Data

class CustomQueryBuilder : ReturnTypeBuilder() {

    fun findAllServices(pagination: PaginationInput? = null, block: ServiceReturnTypeBuilder.() -> Unit) = apply {
        val builder = ServiceReturnTypeBuilder().apply(block)

        if (pagination == null) withField("findAllServices", builder)
        else                    withField("findAllServices", builder, "pagination" to pagination)
    }

    fun findAllServicesByHost(host: String, block: ServiceReturnTypeBuilder.() -> Unit) = apply {
        val builder = ServiceReturnTypeBuilder().apply(block)

        withField("findAllServicesByHost", builder, "host" to StringInput(host))
    }

    fun findAllServicesByTopic(topic: String, block: ServiceReturnTypeBuilder.() -> Unit) = apply {
        val builder = ServiceReturnTypeBuilder().apply(block)

        withField("findAllServicesByTopic", builder, "topic" to StringInput(topic))
    }

    fun findAllServicesSortedByTopic(block: TopicGroupReturnTypeBuilder.() -> Unit) = apply {
        val builder = TopicGroupReturnTypeBuilder().apply(block)

        withField("findAllServicesSortedByTopic", builder)
    }

    fun findServiceById(id: String, block: ServiceReturnTypeBuilder.() -> Unit) = apply {
        val builder = ServiceReturnTypeBuilder().apply(block)

        withField("findServiceById", builder, "id" to StringInput(id))
    }

}