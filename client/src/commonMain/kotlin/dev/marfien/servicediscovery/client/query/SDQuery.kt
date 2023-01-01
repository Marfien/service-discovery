package dev.marfien.servicediscovery.client.query

import com.apollographql.apollo3.api.CustomScalarAdapters
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.api.json.JsonWriter
import dev.marfien.servicediscovery.json.JsonWriterBridge
import dev.marfien.servicediscovery.model.InputType

abstract class SDQuery<D : Query.Data>(
    val variables: Map<String, InputType> = mapOf()
) : Query<D> {

    override fun name(): String = this::class.simpleName ?: "AnonymousSDQuery"

    override fun serializeVariables(writer: JsonWriter, customScalarAdapters: CustomScalarAdapters) = this.variables.takeUnless { it.isEmpty() }?.let {
        val writer = JsonWriterBridge(writer)

        it.forEach { (name, value) -> value.writeJson(writer.name(name)) }

    } ?: Unit

}