package dev.marfien.servicediscovery.model

data class ServiceEvent(val type: ServiceEventType, val service: RegisteredService)

enum class ServiceEventType {

    REGISTRATION, REMOVAL, UPDATE

}