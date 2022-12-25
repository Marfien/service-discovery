package dev.marfien.servicediscovery.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("services")
data class RegisteredService(
    @Id var id: String?,
    override var network: Network,
    override var topic: String,
//    var metadata: Map<String, Any> TODO
) : Service {

//    @Indexed(name = "ttl_index", expireAfterSeconds = 15) // TODO create constant
    var lastHealthSignal = Date()

}

fun Service.toRegisteredService(): RegisteredService {
    return when (this) {
        is RegisteredService -> this
        is AnonymousService -> RegisteredService(null, this.network, this.topic)
        else -> throw NotImplementedError("Not proper implementation: ${this::class.java}")
    }
}