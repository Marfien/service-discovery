package dev.marfien.servicediscovery.json

import com.apollographql.apollo3.api.json.JsonWriter as ApolloJsonWriter

class JsonWriterBridge(
    private val writer: ApolloJsonWriter
) : JsonWriter {

    override fun beginArray(): JsonWriter = apply { this.writer.beginArray() }

    override fun endArray(): JsonWriter = apply { this.writer.endArray() }

    override fun beginObject(): JsonWriter = apply { this.writer.beginObject() }

    override fun endObject(): JsonWriter = apply { this.writer.endObject() }

    override fun name(name: String): JsonWriter = apply { this.writer.name(name) }

    override fun value(value: String): JsonWriter = apply { this.writer.value(value) }

    override fun nullValue(): JsonWriter = apply { this.writer.nullValue() }

    override fun value(value: Boolean): JsonWriter = apply { this.writer.value(value) }

    override fun value(value: Double): JsonWriter = apply { this.writer.value(value) }

    override fun value(value: Int): JsonWriter = apply { this.writer.value(value) }

    override fun value(value: Long): JsonWriter = apply { this.writer.value(value) }

    override val path: String
        get() = this.writer.path

    override fun flush() = this.writer.flush()
}