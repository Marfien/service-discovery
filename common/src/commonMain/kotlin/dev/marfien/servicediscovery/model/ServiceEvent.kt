package dev.marfien.servicediscovery.model

data class ServiceEvent(
    val type: ServiceEventType?,
    val service: ServiceType?
) {

    companion object {

        fun create(type: ServiceEventType, service: ServiceType) =
            ServiceEvent(type, service)

    }

}

enum class ServiceEventType {

    REGISTRATION, REMOVAL, UPDATE

}