package dev.marfien.servicediscovery.registry.model

import dev.marfien.servicediscovery.model.NetworkType
import dev.marfien.servicediscovery.model.Service
import dev.marfien.servicediscovery.model.ServiceInput
import dev.marfien.servicediscovery.model.ServiceType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("services")
data class WrappedServiceType(
    @Id var id: String?,
    override var network: NetworkType,
    override var topic: String,
) : Service {

    @Indexed(name = "ttl_index", expireAfterSeconds = 15)
    var lastHealthSignal = Date()

    fun toServiceType() =
        Service.createType(this.id!!, this.topic, this.network)

}

fun Service.toWrappedService(): WrappedServiceType {
    return when (this) {
        is WrappedServiceType -> this
        is ServiceType -> WrappedServiceType(this.id, this.network!!, this.topic!!)
        is ServiceInput -> WrappedServiceType(null, this.network.toType(), this.topic)
        else -> throw NotImplementedError("Not proper implementation: ${this::class.java}")
    }
}