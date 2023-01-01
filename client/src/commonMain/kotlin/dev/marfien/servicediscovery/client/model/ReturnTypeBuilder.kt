package dev.marfien.servicediscovery.client.model

import dev.marfien.servicediscovery.model.DocumentPart

open class ReturnTypeBuilder : DocumentPart {

    private val fields = mutableMapOf<String, ReturnTypeBuilderData>()

    override fun toDocument(): String = this.fields.takeUnless { it.isEmpty() }?.let {

        it.entries.joinToString(
            prefix = "{ ",
            postfix = " }",
            separator = " "
        ) { (k, v) ->
            "$k${v.toDocument()}"
        }

    } ?: throw  throw IllegalStateException("Ether port or host must be used")

    internal fun withField(name: String): Unit {
        this.fields.getOrPut(name) { ScalarReturnTypeBuilderData }
    }

    internal fun withField(name: String, builder: ReturnTypeBuilder) {
        this.fields[name] = ComplexReturnTypeBuilderData(builder)
    }

}

object NoOpReturnTypeBuilder : ReturnTypeBuilder()

internal sealed interface ReturnTypeBuilderData : DocumentPart

object ScalarReturnTypeBuilderData : ReturnTypeBuilderData {

    override fun toDocument(): String = ""
}

internal data class ComplexReturnTypeBuilderData(val builder: ReturnTypeBuilder) : ReturnTypeBuilderData {

    override fun toDocument(): String = " ${this.builder.toDocument()}"
}

class NetworkReturnTypeBuilder : ReturnTypeBuilder() {

    fun withHost() = apply { super.withField("host") }

    fun withPort() = apply { super.withField("port") }

}

class ServiceReturnTypeBuilder : ReturnTypeBuilder() {

    fun withId() = apply {
        super.withField("id")
    }

    fun withTopic() = apply {
        super.withField("topic")
    }

    fun withNetwork(block: (NetworkReturnTypeBuilder) -> Unit) = apply {
        super.withField("network", NetworkReturnTypeBuilder().apply(block))
    }

}