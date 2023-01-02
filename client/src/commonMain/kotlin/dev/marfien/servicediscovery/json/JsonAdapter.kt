package dev.marfien.servicediscovery.json

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.CustomScalarAdapters
import com.apollographql.apollo3.api.json.JsonReader as ApolloJsonReader
import com.apollographql.apollo3.api.json.JsonWriter as ApolloJsonWriter

interface JsonAdapter<T> : Adapter<T> {

    override fun fromJson(reader: ApolloJsonReader, customScalarAdapters: CustomScalarAdapters): T = this.fromJson(JsonReaderBridge(reader))

    override fun toJson(writer: ApolloJsonWriter, customScalarAdapters: CustomScalarAdapters, value: T) = this.toJson(JsonWriterBridge(writer), value)

    fun fromJson(reader: JsonReader): T { TODO("Not implemented yet. (${this::class})") }

    fun toJson(writer: JsonWriter, value: T) { TODO("Not implemented yet. (${this::class})") }

}