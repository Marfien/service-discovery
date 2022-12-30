package dev.marfien.servicediscovery.model

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
) : Service, InputType
