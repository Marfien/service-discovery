package dev.marfien.servicediscovery.client.operation

import com.apollographql.apollo3.api.*
import com.apollographql.apollo3.api.json.JsonReader
import com.apollographql.apollo3.api.json.JsonWriter
import com.apollographql.apollo3.exception.JsonDataException
import com.apollographql.apollo3.exception.JsonEncodingException
import com.benasher44.uuid.Uuid
import dev.marfien.servicediscovery.client.model.constructFields
import dev.marfien.servicediscovery.client.model.toDocumentType
import kotlin.random.Random

enum class OperationType(val asString: String) {

    QUERY("query"),
    MUTATION("mutation"),
    SUBSCRIPTION("subscription");

    override fun toString(): String = this.asString

}

class NamedOperation(
    val operationType: OperationType,
    val operationName: String,
    val sections: List<CompiledSelection>,
    val operationVariables: Collection<OperationVariable>
) : Query<NamedOperationData>, Mutation<NamedOperationData>, Subscription<NamedOperationData> {

    private val id = Uuid(Random.nextLong(), Random.nextLong())
    private val document: String
    private var variables = mutableListOf<SerializableVariable>()

    init {
        this.document =
            "$operationType $operationName${constructVariables()} ${
                constructFields(this.sections)
            }"
    }

    override fun adapter(): Adapter<NamedOperationData> = NamedOperationDataAdapter

    override fun document(): String = this.document

    override fun id(): String = this.id.toString()

    override fun name(): String = this.operationName

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

    private fun constructVariables(): String = this.operationVariables.takeUnless { it.isEmpty() }?.let {
        it.joinToString(
            prefix = "(",
            postfix = ")",
            separator = " "
        ) { variable -> variable.toDocument() }
    } ?: ""

    class Builder(val type: OperationType, val name: String) {

        private var sections = mutableListOf<CompiledSelection>()
        private var variables = mutableListOf<OperationVariable>()

        fun build(): NamedOperation = NamedOperation(
            this.type,
            this.name,
            this.sections,
            this.variables
        )

        fun withSections(sections: List<CompiledSelection>) = this.apply { this.sections = sections as? MutableList ?: sections.toMutableList() }

        fun addSections(sections: Collection<CompiledSelection>) = this.apply { this.sections += sections }

        fun addSection(section: CompiledSelection) = this.apply { this.sections += section }

        fun withOperationVariables(variables: List<OperationVariable>) = this.apply { this.variables = variables as? MutableList ?: variables.toMutableList() }

        fun addOperationVariables(variables: Collection<OperationVariable>) = this.apply { this.variables += variables }

        fun addOperationVariable(variable: OperationVariable) = this.apply { this.variables += variable }

    }

    companion object {

        val type = ObjectType.Builder("Operation").build()

    }

}

data class SerializableVariable(
    val name: CompiledVariable,
    val value: JsonSerializable
)

interface JsonSerializable {
    fun writeToJson(writer: JsonWriter)
}

data class OperationVariable(
    val name: CompiledVariable,
    val type: CompiledType
) {

    fun toDocument(): String = "$${name.name}: ${type.toDocumentType()}"

}

class NamedOperationData(val values: Map<String, Any?>) : Query.Data, Mutation.Data, Subscription.Data


object NamedOperationDataAdapter : Adapter<NamedOperationData> {

    override fun fromJson(reader: JsonReader, customScalarAdapters: CustomScalarAdapters): NamedOperationData {
        return NamedOperationData(readObject(reader))
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

    override fun toJson(writer: JsonWriter, customScalarAdapters: CustomScalarAdapters, value: NamedOperationData) =
        throw JsonEncodingException("NamedOperationData cannot be encoded")

}