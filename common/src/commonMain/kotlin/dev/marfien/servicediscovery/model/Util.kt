package dev.marfien.servicediscovery.model

import dev.marfien.servicediscovery.json.JsonReader
import dev.marfien.servicediscovery.json.JsonWriter

data class TopicGroup(
    val topic: String?,
    val services: List<ServiceType> = listOf()
) {

    companion object {

        fun create(topic: String, services: List<ServiceType>) =
            TopicGroup(topic, services)

        fun fromJson(reader: JsonReader): TopicGroup {
            var topic: String? = null
            var services: MutableList<ServiceType> = mutableListOf()

            reader.nextObject {
                when (it) {
                    "topic" -> topic = reader.nextString()
                    "services" -> services.apply {
                        reader.nextArray { this += ServiceType.fromJson(reader) }
                    }
                    else -> reader.skipValue()
                }
            }

            return TopicGroup(topic, services)
        }

    }

}


data class PaginationInput(
    val page: Int,
    val pageSize: Int
) : InputType {

    override fun toDocument(): String = "{ page: $page pageSize: $pageSize }"

    override fun writeJson(writer: JsonWriter) {
        writer.beginObject()
        writer.name("page").value(this.page)
        writer.name("pageSize").value(this.pageSize)
        writer.endObject()
    }

    companion object {

        fun create(page: Int, pageSize: Int) =
            PaginationInput(page, pageSize)

    }

}