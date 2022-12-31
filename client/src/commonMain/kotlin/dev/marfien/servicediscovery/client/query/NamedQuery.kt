package dev.marfien.servicediscovery.client.query

import com.apollographql.apollo3.api.*
import com.apollographql.apollo3.api.json.JsonReader
import com.apollographql.apollo3.api.json.JsonWriter
import com.apollographql.apollo3.exception.JsonDataException
import com.apollographql.apollo3.exception.JsonEncodingException
import com.benasher44.uuid.Uuid
import dev.marfien.servicediscovery.client.model.constructFields
import dev.marfien.servicediscovery.client.model.toDocumentType
import kotlin.random.Random

class NamedQuery(
    val queryname: String,
    val sections: List<CompiledSelection>,
    val queryVariables: Collection<QueryVariable>
) : Query<NamedQueryData> {

    private val id = Uuid(Random.nextLong(), Random.nextLong())
    private val document: String
    private val variables = mutableListOf<SerializableVariable>()

    init {
        this.document =
            "query $queryname${constructVariables()} ${
                constructFields(this.sections)
            }"
    }

    override fun adapter(): Adapter<NamedQueryData> = NamedQueryDataAdapter

    override fun document(): String = this.document

    override fun id(): String = this.id.toString()

    override fun name(): String = this.queryname

    override fun rootField(): CompiledField =
        CompiledField.Builder("data", type)
            .selections(this.sections)
            .build()

    override fun serializeVariables(writer: JsonWriter, customScalarAdapters: CustomScalarAdapters) {
        writer.beginObject()

        this.variables.forEach {
            writer.name(it.name.name)
            it.value.writeToJson(writer)
        }

        writer.endObject()
    }

    fun constructVariables(): String = this.queryVariables.takeUnless { it.isEmpty() }?.let {
        it.joinToString(
            prefix = "(",
            postfix = ")",
            separator = " "
        ) { variable -> variable.toDocument() }
    } ?: ""

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

        fun withQueryVariables(variables: List<QueryVariable>) = this.apply { this.variables = variables as? MutableList ?: variables.toMutableList() }

        fun addQueryVariables(variables: Collection<QueryVariable>) = this.apply { this.variables += variables }

        fun addQueryVariable(variable: QueryVariable) = this.apply { this.variables += variable }

    }

    companion object {

        val type = ObjectType.Builder("Query").build()

    }

}

data class SerializableVariable(
    val name: CompiledVariable,
    val value: JsonSerializable
)

interface JsonSerializable { fun writeToJson(writer: JsonWriter) }

data class QueryVariable(
    val name: CompiledVariable,
    val type: CompiledType
) {

    fun toDocument(): String = "$${name.name}: ${type.toDocumentType()}"

}

class NamedQueryData(val values: Map<String, Any?>) : Query.Data


object NamedQueryDataAdapter : Adapter<NamedQueryData> {

    override fun fromJson(reader: JsonReader, customScalarAdapters: CustomScalarAdapters): NamedQueryData {
        return NamedQueryData(readObject(reader))
    }

    fun readJson(reader: JsonReader): Any? = when (val token = reader.peek()) {
        JsonReader.Token.NULL -> null
        JsonReader.Token.BEGIN_ARRAY -> readArray(reader)
        JsonReader.Token.BEGIN_OBJECT -> readObject(reader)
        JsonReader.Token.STRING -> reader.nextString()
        JsonReader.Token.BOOLEAN -> reader.nextBoolean()
        JsonReader.Token.LONG -> reader.nextLong()
        JsonReader.Token.NUMBER -> reader.nextDouble()
        else -> throw JsonDataException("Invalid token found: $token (Path: ${
            reader.getPath()
                .joinToString(".") { 
                    when (it) { 
                        is Int -> "[$it]"
                        is String -> "\"$it\""
                        else -> throw IllegalArgumentException("Found suspicious type in path: ${it::class}")
                    }
                } 
        })")

    }

    fun readObject(reader: JsonReader): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()

        reader.beginObject()

        while (reader.peek() != JsonReader.Token.END_OBJECT) {
            val key = reader.nextName()
            val value = readJson(reader)

            map[key] = value
        }

        reader.endObject()
        return map
    }

    fun readArray(reader: JsonReader): List<Any?> {
        val list = mutableListOf<Any?>()

        reader.beginArray()

        while (reader.peek() != JsonReader.Token.END_ARRAY) {
            list += readJson(reader)
        }

        reader.endArray()
        return list
    }

    override fun toJson(writer: JsonWriter, customScalarAdapters: CustomScalarAdapters, value: NamedQueryData) =
        throw JsonEncodingException("NamedQueryData cannot be encoded")

}