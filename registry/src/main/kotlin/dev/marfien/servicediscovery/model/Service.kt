package dev.marfien.servicediscovery.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("services")
data class Service(
    @Id var id: String?,
    var network: Network,
    var topic: String,
//    var metadata: Map<String, Any> TODO
) {

    @Indexed(name = "ttl_index", expireAfterSeconds = 15) // TODO create constant
    var lastHealthSignal = Date()

}

data class AnonymousService(var network: Network, var topic: String) {//, var metadata: Map<String, Any>?) { TODO

    fun asService(): Service = Service(null, this.network, this.topic)//, this.metadata ?: mapOf())

}