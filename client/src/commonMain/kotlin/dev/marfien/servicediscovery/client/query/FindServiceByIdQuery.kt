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

class FindServiceByIdQuery(
    id: String,
    block: ServiceReturnTypeBuilder.() -> Unit
) : SDQuery<FindServiceByIdData>(
    CustomSDQueryBuilder().findServiceById(id, block)
) {

    override fun adapter(): Adapter<FindServiceByIdData> = FindServiceByIdDataAdapter

    override fun id(): String = id.toString()

    companion object {

        val id = Uuid(Random.nextLong(), Random.nextLong())

    }

}

data class FindServiceByIdData(
    val service: ServiceType?
) : Query.Data

object FindServiceByIdDataAdapter : JsonAdapter<FindServiceByIdData> {

    override fun fromJson(reader: JsonReader): FindServiceByIdData = FindServiceByIdData(
        if (reader.peek() != JsonReader.Token.NULL) ServiceType.fromJson(reader)
        else                                        reader.nextNull()
    )
}