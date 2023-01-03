package dev.marfien.servicediscovery.client

import com.apollographql.apollo3.api.CompiledField
import com.apollographql.apollo3.api.CustomScalarAdapters
import com.apollographql.apollo3.api.json.JsonWriter
import dev.marfien.servicediscovery.client.model.ReturnTypeBuilder

abstract class SDOperation internal constructor(
    private val type: OperationType,
//    val variables: Map<String, InputType> = mapOf(),
    private val builder: ReturnTypeBuilder
) {

    fun name(): String = this::class.simpleName ?: "AnonymousSD${type.toString().replaceFirstChar { it.uppercaseChar() }}"

    fun document(): String =
        "$type ${this.name()} ${
            this.builder.toDocument()
        }"

    fun rootField(): CompiledField {
        // only needed for caching and normalization
        // should be unnecessary 'cause we don't need caching etc.
        TODO("Not yet implemented")
    }

    fun serializeVariables(writer: JsonWriter, customScalarAdapters: CustomScalarAdapters) = Unit //this.variables.takeUnless { it.isEmpty() }?.let {
//        val bridge = JsonWriterBridge(writer)
//
//        it.forEach { (name, value) -> value.writeJson(bridge.name(name)) }
//
//    } ?: Unit

}

internal enum class OperationType() {

    QUERY,
    MUTATION,
    SUBSCRIPTION;

    override fun toString(): String = this.name.lowercase()

}