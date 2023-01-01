package dev.marfien.servicediscovery.json

interface JsonWriter {

    fun beginArray(): JsonWriter

    fun endArray(): JsonWriter

    fun beginObject(): JsonWriter

    fun endObject(): JsonWriter

    fun name(name: String): JsonWriter

    fun value(value: String): JsonWriter

    fun nullValue(): JsonWriter

    fun value(value: Boolean): JsonWriter

    fun value(value: Double): JsonWriter

    fun value(value: Int): JsonWriter

    fun value(value: Long): JsonWriter

    val path: String

    fun flush()
}