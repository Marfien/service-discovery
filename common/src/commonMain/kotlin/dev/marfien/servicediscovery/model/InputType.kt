package dev.marfien.servicediscovery.model

import dev.marfien.servicediscovery.json.JsonWriter

sealed interface InputType : DocumentPart {

    fun writeJson(writer: JsonWriter)

}

data class StringInput(val value: String) : InputType {

    override fun toDocument(): String = "\"$value\""

    override fun writeJson(writer: JsonWriter) { writer.value(this.value) }
}

data class IntInput(val value: Long) : InputType {

    override fun toDocument(): String = this.value.toString()

    override fun writeJson(writer: JsonWriter) { writer.value(this.value) }
}

data class FloatInput(val value: Double) : InputType {

    override fun toDocument(): String = this.value.toString()

    override fun writeJson(writer: JsonWriter) { writer.value(this.value) }
}

data class BooleanInput(val value: Long) : InputType {

    override fun toDocument(): String = this.value.toString()

    override fun writeJson(writer: JsonWriter) { writer.value(this.value) }
}

data class ListInput<I : InputType>(val list: List<I>) : InputType {

    override fun toDocument(): String = this.list.joinToString(prefix = "[", postfix = "]", separator = " ") { it.toDocument() }

    override fun writeJson(writer: JsonWriter) {
        writer.beginArray()

        this.list.forEach { it.writeJson(writer) }

        writer.endArray()
    }
}