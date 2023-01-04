package dev.marfien.servicediscovery.client.query

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.Query
import com.benasher44.uuid.Uuid
import dev.marfien.servicediscovery.client.model.ServiceReturnTypeBuilder
import dev.marfien.servicediscovery.json.JsonAdapter
import dev.marfien.servicediscovery.json.JsonReader
import dev.marfien.servicediscovery.model.ServiceType
import kotlin.random.Random

class FindAllServicesByTopicQuery(
    host: String,
    block: ServiceReturnTypeBuilder.() -> Unit
) : SDQuery<FindAllServicesByTopicData>(
    CustomSDQueryBuilder().findAllServicesByTopic(host, block)
) {

    override fun adapter(): Adapter<FindAllServicesByTopicData> = FindAllServicesByTopicDataAdapter

    override fun id(): String = id.toString()

    companion object {

        val id = Uuid(Random.nextLong(), Random.nextLong())

    }

}

data class FindAllServicesByTopicData(
    val services: List<ServiceType>
) : Query.Data

object FindAllServicesByTopicDataAdapter : JsonAdapter<FindAllServicesByTopicData> {

    override fun fromJson(reader: JsonReader): FindAllServicesByTopicData = FindAllServicesByTopicData(
        mutableListOf<ServiceType>().apply {
            reader.nextArray {
                this += ServiceType.fromJson(reader)
            }
        }
    )
}