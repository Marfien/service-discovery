package dev.marfien.servicediscovery.model

import dev.marfien.servicediscovery.json.JsonReader
import dev.marfien.servicediscovery.json.JsonWriter

interface Network {

    val host: String?
    val port: Int?

    fun toInput(): NetworkInput = when(this) {
        is NetworkInput -> this
        is NetworkType -> NetworkInput(this.host!!, this.port!!)
        else -> throw NotImplementedError()
    }

    fun toType(): NetworkType = when(this) {
        is NetworkType -> this
        is NetworkInput -> NetworkType(this.host, this.port)
        else -> throw NotImplementedError()
    }

    companion object {

        fun createInputData(host: String, port: Int) = NetworkInput(host, port)

        fun createType(host: String?, port: Int?) = NetworkType(host, port)

    }

}

data class NetworkInput internal constructor(
    override val host: String,
    override val port: Int
) : Network, InputType {

    override fun toDocument(): String = "{ host: \"$host\" port: $port }"

    override fun writeJson(writer: JsonWriter) {
        writer.beginObject()
        writer.name("host").value(this.host)
        writer.name("port").value(this.port)
        writer.endArray()
    }

}


data class NetworkType internal constructor(
    override val host: String?,
    override val port: Int?
) : Network {
    companion object {
        fun fromJson(reader: JsonReader): NetworkType {
            var host: String? = null
            var port: Int? = null

            reader.nextObject {
                when (it) {
                    "host" -> host = reader.nextString()
                    "port" -> port = reader.nextInt()
                    else -> reader.skipValue()
                }
            }

            return NetworkType(host, port)
        }
    }
}