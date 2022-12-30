package dev.marfien.servicediscovery.client.operation

import com.apollographql.apollo3.api.*
import com.apollographql.apollo3.api.json.JsonReader
import com.apollographql.apollo3.api.json.JsonWriter
import com.benasher44.uuid.Uuid
import dev.marfien.servicediscovery.client.model.toDocumentType
import kotlin.random.Random

class NamedQuery(
    val queryname: String,
    val sections: List<CompiledSelection>,
    val variables: Collection<QueryVariable>
) : Query<NamedQueryData> {

    private val id = Uuid(Random.nextLong(), Random.nextLong())
    private val document: String

    init {
        this.document = ""
    }

    override fun adapter(): Adapter<NamedQueryData> {
        TODO("Not yet implemented")
    }

    override fun document(): String = this.document

    override fun id(): String = this.id.toString()

    override fun name(): String = this.queryname

    override fun rootField(): CompiledField =
        CompiledField.Builder("data", type)
            .selections(this.sections)
            .build()

    override fun serializeVariables(writer: JsonWriter, customScalarAdapters: CustomScalarAdapters) {
        TODO("Not yet implemented")
    }

    fun constructVariables(): String {

    }

    class Builder(val name: String) {

        private var sections = mutableListOf<CompiledSelection>()
        private var variables = mutableListOf<QueryVariable>()

        fun build(): NamedQuery = NamedQuery(
            this.name,
            this.sections,
            this.variables
        )

        fun withSections(sections: List<CompiledSelection>) = this.apply { this.sections = sections as? MutableList ?: sections.toMutableList() }

        fun addSections(sections: Collection<CompiledSelection>) = this.apply { this.sections += sections }

        fun addSection(section: CompiledSelection) = this.apply { this.sections += section }

        fun withVariables(variables: List<QueryVariable>) = this.apply { this.variables = variables as? MutableList ?: variables.toMutableList() }

        fun addVariables(variables: Collection<QueryVariable>) = this.apply { this.variables += variables }

        fun addVariable(variable: QueryVariable) = this.apply { this.variables += variable }

        fun buildWithVariables(variables: List<QueryVariable>) = NamedQuery(this.name, this.sections, variables)

        fun buildWithVariables(vararg variables: QueryVariable) = buildWithVariables(variables.toList())

    }

    companion object {

        val type = ObjectType.Builder("Query").build()

    }

}

data class QueryVariable(
    val name: CompiledVariable,
    val type: CompiledType
) {

    fun toDocument(): String = "$${name.name}: ${type.toDocumentType()}"

}

class NamedQueryData : Query.Data


object NamedQueryDataAdapter : Adapter<NamedQueryData> {

    override fun fromJson(reader: JsonReader, customScalarAdapters: CustomScalarAdapters): NamedQueryData {
        TODO("Not yet implemented")
    }

    override fun toJson(writer: JsonWriter, customScalarAdapters: CustomScalarAdapters, value: NamedQueryData) {
        TODO("Not yet implemented")
    }
}