package dev.marfien.servicediscovery.client.query

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.CompiledField
import com.apollographql.apollo3.api.CustomScalarAdapters
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.api.json.JsonWriter
import dev.marfien.servicediscovery.model.InputType

abstract class SDQuery<D : Query.Data>(
    val variables: Map<String, InputType>
) : Query<D> {

    override fun adapter(): Adapter<D> {
        TODO("Not yet implemented")
    }

    override fun document(): String {
        TODO("Not yet implemented")
    }

    override fun name(): String = this::class.simpleName ?: "AnonymousSDQuery"

    override fun rootField(): CompiledField {
        TODO("Not yet implemented")
    }

    override fun serializeVariables(writer: JsonWriter, customScalarAdapters: CustomScalarAdapters) = this.variables.takeUnless { it.isEmpty() }?.forEach { (key, value) ->


    } ?: Unit

}