package dev.marfien.servicediscovery.model

import dev.marfien.servicediscovery.json.JsonWriter

sealed interface InputType {

    fun toDocument(): String

    fun writeJson(writer: JsonWriter)

}