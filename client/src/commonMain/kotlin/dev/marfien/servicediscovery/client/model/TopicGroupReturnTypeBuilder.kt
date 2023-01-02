package dev.marfien.servicediscovery.client.model

class TopicGroupReturnTypeBuilder : ReturnTypeBuilder() {

    fun withTopic() = apply { super.withField("topic") }

    fun withServices(block: ServiceReturnTypeBuilder.() -> Unit) = apply {
        super.withField("services", ServiceReturnTypeBuilder().apply(block))
    }

}