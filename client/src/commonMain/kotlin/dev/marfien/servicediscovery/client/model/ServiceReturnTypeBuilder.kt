package dev.marfien.servicediscovery.client.model

class ServiceReturnTypeBuilder : ReturnTypeBuilder() {

    fun withId() = apply {
        super.withField("id")
    }

    fun withTopic() = apply {
        super.withField("topic")
    }

    fun withNetwork(block: NetworkReturnTypeBuilder.() -> Unit) = apply {
        super.withField("network", NetworkReturnTypeBuilder().apply(block))
    }

}