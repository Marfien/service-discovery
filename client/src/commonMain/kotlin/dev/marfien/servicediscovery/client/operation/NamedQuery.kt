package dev.marfien.servicediscovery.client.operation

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.CompiledField
import com.apollographql.apollo3.api.CustomScalarAdapters
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.api.json.JsonWriter
import com.benasher44.uuid.Uuid
import kotlin.random.Random

class NamedQuery(
    val queryname: String
) : Query<QueryOperationData> {

    private val id = Uuid(Random.nextLong(), Random.nextLong())

    override fun adapter(): Adapter<QueryOperationData> {
        TODO("Not yet implemented")
    }

    override fun document(): String

    override fun id(): String = this.id.toString()

    override fun name(): String = this.queryname

    override fun rootField(): CompiledField {
        TODO("Not yet implemented")
    }

    override fun serializeVariables(writer: JsonWriter, customScalarAdapters: CustomScalarAdapters) {
        TODO("Not yet implemented")
    }
}

class QueryOperationData : Query.Data