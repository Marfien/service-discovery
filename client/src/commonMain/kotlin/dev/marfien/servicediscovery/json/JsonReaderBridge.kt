package dev.marfien.servicediscovery.json

import com.apollographql.apollo3.api.json.JsonReader as ApolloJsonReader

class JsonReaderBridge(
    private var reader: ApolloJsonReader
) : JsonReader {

    override fun peek(): JsonReader.Token = JsonReader.Token.valueOf(this.reader.peek().name)

    override fun beginArray(): JsonReader = apply { this.reader.beginArray() }

    override fun endArray(): JsonReader = apply { this.reader.endArray() }

    override fun beginObject(): JsonReader = apply { this.reader.beginObject() }

    override fun endObject(): JsonReader = apply { this.reader.endObject() }

    override fun hasNext(): Boolean = this.reader.hasNext()

    override fun nextName(): String = this.reader.nextName()

    override fun nextString(): String? = this.reader.nextString()

    override fun nextBoolean(): Boolean = this.reader.nextBoolean()

    override fun nextNull(): Nothing? = this.reader.nextNull()

    override fun nextInt(): Int = this.reader.nextInt()

    override fun nextDouble(): Double = this.reader.nextDouble()

    override fun nextLong(): Long = this.reader.nextLong()

    override fun skipValue() = this.reader.skipValue()

    override fun selectName(names: List<String>): Int = this.reader.selectName(names)

    override fun rewind() = this.reader.rewind()

    override fun getPath(): List<Any> = this.reader.getPath()

}