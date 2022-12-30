package dev.marfien.servicediscovery.client.operation

import com.apollographql.apollo3.api.*
import com.apollographql.apollo3.api.json.JsonWriter
import com.benasher44.uuid.Uuid
import dev.marfien.servicediscovery.client.model.constructFields
import dev.marfien.servicediscovery.client.model.toDocument
import dev.marfien.servicediscovery.client.model.toDocumentType
import kotlin.random.Random

class NamedQuery(
    val queryname: String
) : Query<QueryOperationData> {

    private val id = Uuid(Random.nextLong(), Random.nextLong())

    private val selections = mutableListOf<CompiledSelection>()
    private val variables = mutableListOf<QueryVariable>()
    private val root = CompiledField.Builder(this.queryname, ObjectType.Builder("Query").build()).build()

    override fun adapter(): Adapter<QueryOperationData> {
        TODO("Not yet implemented")
    }

    override fun document(): String = this.root.selections.takeUnless { it.isEmpty() }?.let {
        "query $queryname${constructVariables()}${
            constructFields(it)
        }"
    } ?: throw IllegalStateException("CompiledField has no selections. Cannot execute empty query")

    override fun id(): String = this.id.toString()

    override fun name(): String = this.queryname

    override fun rootField(): CompiledField = this.root

    override fun serializeVariables(writer: JsonWriter, customScalarAdapters: CustomScalarAdapters) {
        TODO("Not yet implemented")
    }

    fun constructVariables(): String {

    }
}

data class QueryVariable(
    val name: CompiledVariable,
    val type: CompiledType
) {

    fun toDocument(): String = "$${name.name}: ${type.toDocumentType()}"

}

class QueryOperationData : Query.Data