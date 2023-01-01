package dev.marfien.servicediscovery.model

sealed interface InputType {

    fun toDocument(): String

}