package dev.marfien.servicediscovery.client.mutation

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.Mutation
import com.benasher44.uuid.Uuid
import dev.marfien.servicediscovery.json.JsonAdapter
import dev.marfien.servicediscovery.json.JsonReader
import kotlin.random.Random

class RemoveServiceMutation(
    id: String
) : SDMutation<RemoveServiceData>(
    CustomSDMutationBuilder().removeService(id)
) {

    override fun adapter(): Adapter<RemoveServiceData> = RemoveServiceDataAdapter

    override fun id(): String = id.toString()

    companion object {

        val id = Uuid(Random.nextLong(), Random.nextLong())

    }

}

data class RemoveServiceData(
    val existed: Boolean
) : Mutation.Data

object RemoveServiceDataAdapter : JsonAdapter<RemoveServiceData> {

    override fun fromJson(reader: JsonReader): RemoveServiceData = RemoveServiceData(
        reader.nextBoolean()
    )
}