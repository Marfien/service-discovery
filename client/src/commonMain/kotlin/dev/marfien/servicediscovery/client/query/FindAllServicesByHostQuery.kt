package dev.marfien.servicediscovery.client.query

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.CompiledField
import com.apollographql.apollo3.api.Query
import com.benasher44.uuid.Uuid
import dev.marfien.servicediscovery.client.model.ServiceReturnTypeBuilder
import dev.marfien.servicediscovery.json.JsonAdapter
import dev.marfien.servicediscovery.json.JsonReader
import dev.marfien.servicediscovery.json.JsonWriter
import dev.marfien.servicediscovery.model.ServiceType
import kotlin.random.Random

class FindAllServicesByHostQuery(
    host: String,
    block: ServiceReturnTypeBuilder.() -> Unit
) : SDQuery<FindAllServicesByHostData>(
    CustomSDQueryBuilder().findAllServicesByHost(host, block)
) {

    override fun adapter(): Adapter<FindAllServicesByHostData> = FindAllServicesByHostDataAdapter

    override fun id(): String = id.toString()

    companion object {

        val id = Uuid(Random.nextLong(), Random.nextLong())

    }

}

data class FindAllServicesByHostData(
    val services: List<ServiceType>
) : Query.Data

object FindAllServicesByHostDataAdapter : JsonAdapter<FindAllServicesByHostData> {

    override fun fromJson(reader: JsonReader): FindAllServicesByHostData = FindAllServicesByHostData(
        mutableListOf<ServiceType>().apply {
            reader.nextArray {
                this += ServiceType.fromJson(reader)
            }
        }
    )
}