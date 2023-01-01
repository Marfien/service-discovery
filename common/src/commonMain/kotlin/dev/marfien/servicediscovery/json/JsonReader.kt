package dev.marfien.servicediscovery.json

interface JsonReader {

    fun peek(): Token

    fun beginArray(): JsonReader

    fun endArray(): JsonReader

    fun beginObject(): JsonReader

    fun endObject(): JsonReader

    fun hasNext(): Boolean

    fun nextName(): String

    fun nextString(): String?

    fun nextBoolean(): Boolean

    fun nextNull(): Nothing?

    fun nextInt(): Int

    fun nextDouble(): Double

    fun nextLong(): Long

    fun skipValue()

    fun selectName(names: List<String>): Int

    fun rewind()

    fun getPath(): List<Any>

    enum class Token {

        BEGIN_ARRAY,
        END_ARRAY,
        BEGIN_OBJECT,
        END_OBJECT,
        NAME,
        STRING,
        NUMBER,
        LONG,
        BOOLEAN,
        NULL,
        END_DOCUMENT,
        ANY

    }
}