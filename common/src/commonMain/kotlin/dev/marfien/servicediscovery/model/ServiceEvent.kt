package dev.marfien.servicediscovery.model

import dev.marfien.servicediscovery.json.JsonReader
import dev.marfien.servicediscovery.json.JsonWriter

data class ServiceEvent(
    val type: ServiceEventType?,
    val service: ServiceType?
) {

    companion object {

        fun create(type: ServiceEventType, service: ServiceType) =
            ServiceEvent(type, service)

        fun fromJson(reader: JsonReader): ServiceEvent {
            var type: ServiceEventType? = null
            var service: ServiceType? = null

            reader.nextObject {
                when (it) {
                    "type" -> type = ServiceEventType.valueOf(reader.nextString()!!)
                    "service" -> service = ServiceType.fromJson(reader)
                    else -> reader.skipValue()
                }
            }

            return ServiceEvent(type, service)
        }

    }

}

enum class ServiceEventType : InputType {

    REGISTRATION, REMOVAL, UPDATE;

    override fun toDocument(): String = this.name

    override fun writeJson(writer: JsonWriter) { writer.value (this.name) }

}