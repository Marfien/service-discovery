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
import dev.marfien.servicediscovery.model.ServiceType
import kotlin.random.Random

class FindAllServicesQuery(
    builder: ServiceReturnTypeBuilder
) : SDQuery<FindAllServicesData>(
    ReturnTypeBuilder()
) {

    override fun adapter(): Adapter<FindAllServicesData> = FindAllServicesDataAdapter

    override fun document(): String {
        TODO("Not yet implemented")
    }

    override fun id(): String = id.toString()

    override fun rootField(): CompiledField {
        TODO("Not yet implemented")
    }

    companion object {

        val id = Uuid(Random.nextLong(), Random.nextLong())

    }

}

data class FindAllServicesData(
    val services: Collection<ServiceType>
) : Query.Data

object FindAllServicesDataAdapter : Adapter<FindAllServicesData> {

    override fun fromJson(reader: JsonReader, customScalarAdapters: CustomScalarAdapters): FindAllServicesData {
        TODO("Not yet implemented")
    }

    override fun toJson(writer: JsonWriter, customScalarAdapters: CustomScalarAdapters, value: FindAllServicesData) {
        TODO("Not yet implemented")
    }
}