package dev.marfien.servicediscovery.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("services")
data class Service(
    @Id var id: String?,
    var network: Network,
    var topic: String,
//    var metadata: Map<String, Any> TODO
)

data class AnonymousService(var network: Network, var topic: String) {//, var metadata: Map<String, Any>?) { TODO

    fun asService(): Service = Service(null, this.network, this.topic)//, this.metadata ?: mapOf())

}