package dev.marfien.servicediscovery.client.query

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.CompiledField
import com.apollographql.apollo3.api.Query
import com.benasher44.uuid.Uuid
import dev.marfien.servicediscovery.client.model.ServiceReturnTypeBuilder
import dev.marfien.servicediscovery.json.JsonAdapter
import dev.marfien.servicediscovery.json.JsonReader
import dev.marfien.servicediscovery.json.JsonWriter
import dev.marfien.servicediscovery.model.PaginationInput
import dev.marfien.servicediscovery.model.ServiceType
import kotlin.random.Random

class FindAllServicesQuery(
    pagination: PaginationInput? = null,
    block: ServiceReturnTypeBuilder.() -> Unit
) : SDQuery<FindAllServicesData>(
    CustomSDQueryBuilder().findAllServices(pagination, block)
) {

    override fun adapter(): Adapter<FindAllServicesData> = FindAllServicesDataAdapter

    override fun id(): String = id.toString()

    companion object {

        val id = Uuid(Random.nextLong(), Random.nextLong())

    }

}

data class FindAllServicesData(
    val services: List<ServiceType>
) : Query.Data

object FindAllServicesDataAdapter : JsonAdapter<FindAllServicesData> {

    override fun fromJson(reader: JsonReader): FindAllServicesData = FindAllServicesData(
        mutableListOf<ServiceType>().apply {
            reader.nextArray {
                this += ServiceType.fromJson(reader)
            }
        }
    )

    override fun toJson(writer: JsonWriter, value: FindAllServicesData) {
        TODO("Not yet implemented")
    }
}