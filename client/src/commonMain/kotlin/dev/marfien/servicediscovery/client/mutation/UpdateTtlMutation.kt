package dev.marfien.servicediscovery.client.mutation

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.Mutation
import com.benasher44.uuid.Uuid
import dev.marfien.servicediscovery.json.JsonAdapter
import dev.marfien.servicediscovery.json.JsonReader
import kotlin.random.Random

class UpdateTtlMutation(
    id: String
) : SDMutation<UpdateTtlData>(
    CustomSDMutationBuilder().updateTTL(id)
) {

    override fun adapter(): Adapter<UpdateTtlData> = UpdateTtlDataAdapter

    override fun id(): String = id.toString()

    companion object {

        val id = Uuid(Random.nextLong(), Random.nextLong())

    }

}

data class UpdateTtlData(
    val exists: Boolean
) : Mutation.Data

object UpdateTtlDataAdapter : JsonAdapter<UpdateTtlData> {

    override fun fromJson(reader: JsonReader): UpdateTtlData = UpdateTtlData(
        reader.nextBoolean()
    )
}