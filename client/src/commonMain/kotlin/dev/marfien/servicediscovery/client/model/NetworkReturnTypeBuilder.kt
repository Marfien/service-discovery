package dev.marfien.servicediscovery.client.model

class NetworkReturnTypeBuilder : ReturnTypeBuilder() {

    fun withHost() = apply { super.withField("host") }

    fun withPort() = apply { super.withField("port") }

}