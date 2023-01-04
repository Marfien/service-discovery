package dev.marfien.servicediscovery.client.query

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.Query
import com.benasher44.uuid.Uuid
import dev.marfien.servicediscovery.client.model.TopicGroupReturnTypeBuilder
import dev.marfien.servicediscovery.json.JsonAdapter
import dev.marfien.servicediscovery.json.JsonReader
import dev.marfien.servicediscovery.model.TopicGroup
import kotlin.random.Random

class FindAllServicesSortedByTopicQuery(
    block: TopicGroupReturnTypeBuilder.() -> Unit
) : SDQuery<FindAllServicesSortedByTopicData>(
    CustomSDQueryBuilder().findAllServicesSortedByTopic(block)
) {

    override fun adapter(): Adapter<FindAllServicesSortedByTopicData> = FindAllServicesSortedByTopicDataAdapter

    override fun id(): String = id.toString()

    companion object {

        val id = Uuid(Random.nextLong(), Random.nextLong())

    }

}

data class FindAllServicesSortedByTopicData(
    val topicGroups: List<TopicGroup>
) : Query.Data

object FindAllServicesSortedByTopicDataAdapter : JsonAdapter<FindAllServicesSortedByTopicData> {

    override fun fromJson(reader: JsonReader): FindAllServicesSortedByTopicData = FindAllServicesSortedByTopicData(
        mutableListOf<TopicGroup>().apply {
            reader.nextArray {
                this += TopicGroup.fromJson(reader)
            }
        }
    )
}