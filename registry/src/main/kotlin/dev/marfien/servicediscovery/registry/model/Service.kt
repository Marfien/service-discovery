package dev.marfien.servicediscovery.registry.model

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("services")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Service(
    @Id var id: String?,
    var network: Network,
    var topic: String,
    var metadata: String
)

data class AnonymousService(var network: Network, var topic: String, var metadata: String?) {

    fun asService(): Service = Service(null, this.network, this.topic, this.metadata ?: "{}")

}

data class Network(val host: String, val port: Int)