package dev.marfien.servicediscovery.client.query

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.CompiledField
import com.apollographql.apollo3.api.CustomScalarAdapters
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.api.json.JsonWriter
import com.benasher44.uuid.Uuid
import dev.marfien.servicediscovery.client.OperationType
import dev.marfien.servicediscovery.client.SDOperation
import dev.marfien.servicediscovery.client.model.ReturnTypeBuilder
import dev.marfien.servicediscovery.client.model.ServiceReturnTypeBuilder
import dev.marfien.servicediscovery.client.model.TopicGroupReturnTypeBuilder
import dev.marfien.servicediscovery.json.JsonAdapter
import dev.marfien.servicediscovery.json.JsonReader
import dev.marfien.servicediscovery.model.PaginationInput
import dev.marfien.servicediscovery.model.ServiceType
import dev.marfien.servicediscovery.model.StringInput
import dev.marfien.servicediscovery.model.TopicGroup
import dev.marfien.servicediscovery.servicediscovery.client.ServiceQuery
import kotlin.random.Random

abstract class SDQuery<D : Query.Data>(
//    val variables: Map<String, InputType> = mapOf(),
    builder: ReturnTypeBuilder
) : SDOperation(OperationType.QUERY, builder), Query<D> {

    companion object {

        fun create(block: CustomSDQueryBuilder.() -> Unit) = CustomSDQuery(CustomSDQueryBuilder().apply(block))

    }

}

class CustomSDQuery internal constructor(
    builder: CustomSDQueryBuilder
) : SDQuery<CustomSDQueryData>(builder) {

    override fun adapter(): Adapter<CustomSDQueryData> = CustomSDQueryDataAdapter

    override fun id(): String = id.toString()

    companion object {

        val id = Uuid(Random.nextLong(), Random.nextLong())

    }

}

object CustomSDQueryDataAdapter : JsonAdapter<CustomSDQueryData> {

    override fun fromJson(reader: JsonReader): CustomSDQueryData {
        val data = CustomSDQueryData()

        while (reader.hasNext()) {
            when (val name = reader.nextName()) {
                "findAllServices" -> data.findAllServices = FindAllServicesDataAdapter.fromJson(reader).services
                "findAllServicesByHost" -> data.findAllServicesByHost = FindAllServicesByHostDataAdapter.fromJson(reader).services
                "findAllServicesBtTopic" -> data.findAllServicesByTopic = FindAllServicesByTopicDataAdapter.fromJson(reader).services
                "findAllServicesSortedByTopic" -> data.findAllServicesSortedByTopic = FindAllServicesSortedByTopicDataAdapter.fromJson(reader).topicGroups
                "findServiceById" -> data.findServiceById = FindServiceByIdDataAdapter.fromJson(reader).service
                else -> {
                    println("Skipping unknown value: $name")
                    reader.skipValue()
                }
            }
        }

        return data
    }
}

data class CustomSDQueryData(
    var findAllServices: List<ServiceType> = listOf(),
    var findAllServicesByHost: List<ServiceType> = listOf(),
    var findAllServicesByTopic: List<ServiceType> = listOf(),
    var findAllServicesSortedByTopic: List<TopicGroup> = listOf(),
    var findServiceById: ServiceType? = null
) : Query.Data

class CustomSDQueryBuilder internal constructor() : ReturnTypeBuilder() {

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