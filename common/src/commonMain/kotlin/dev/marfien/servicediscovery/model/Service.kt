package dev.marfien.servicediscovery.model

interface Service {
    val network: Network
    val topic: String
}

data class RegisteredService(
    val id: String,
    override val network: Network,
    override val topic: String,
) : Service

data class AnonymousService(
    override val network: Network,
    override val topic: String
) : Service
