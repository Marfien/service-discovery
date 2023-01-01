package dev.marfien.servicediscovery.model

import dev.marfien.servicediscovery.json.JsonWriter

interface Service {

    val network: Network?
    val topic: String?

    companion object {

        fun createType(id: String, topic: String, network: NetworkType) =
            ServiceType(id, topic, network)

        fun createInput(topic: String, network: NetworkInput) =
            ServiceInput(topic, network)

    }

}

data class ServiceType(
    val id: String?,
    override val topic: String?,
    override val network: NetworkType?
) : Service

data class ServiceInput(
    override val topic: String,
    override val network: NetworkInput
) : Service, InputType {

    override fun toDocument(): String = "{ topic: \"$topic\" network: ${network.toDocument()} }"

    override fun writeJson(writer: JsonWriter) {
        writer.beginObject()
        writer.name("topic").value(this.topic)
        this.network.writeJson(writer.name("network"))
        writer.endObject()
    }
}
