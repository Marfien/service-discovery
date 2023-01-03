package dev.marfien.servicediscovery.client.model

class ServiceEventReturnTypeBuilder : ReturnTypeBuilder() {

    fun withType() = apply { super.withField("type") }

    fun withService(block: ServiceReturnTypeBuilder.() -> Unit) = apply {
        super.withField("service", ServiceReturnTypeBuilder().apply(block))
    }

}