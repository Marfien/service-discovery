package dev.marfien.servicediscovery.client.model

import dev.marfien.servicediscovery.model.DocumentPart
import dev.marfien.servicediscovery.model.InputType

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

    internal fun withField(name: String) {
        this.fields.getOrPut(name) { ScalarReturnTypeBuilderData() }
    }

    internal fun withField(name: String, vararg arguments: Pair<String, InputType>) {
        this.fields.getOrPut(name) { ScalarReturnTypeBuilderData(arguments.toMap()) }
    }

    internal fun withField(name: String, builder: ReturnTypeBuilder) {
        this.fields[name] = ComplexReturnTypeBuilderData(builder)
    }

    internal fun withField(name: String, builder: ReturnTypeBuilder, vararg arguments: Pair<String, InputType>) {
        this.fields[name] = ComplexReturnTypeBuilderData(builder, arguments.toMap())
    }

}

object NoOpReturnTypeBuilder : ReturnTypeBuilder()

private sealed interface ReturnTypeBuilderData : DocumentPart {

    val arguments: Map<String, InputType>

}

private fun ReturnTypeBuilderData.constructArguments(): String = this.arguments.entries
    .takeUnless { it.isEmpty() }
    ?.joinToString(separator = " ", prefix = "(", postfix = ")") { (k, v) -> "$k: ${v.toDocument()}" }
    ?: ""

private data class ScalarReturnTypeBuilderData(
    override val arguments: Map<String, InputType> = mapOf()
) : ReturnTypeBuilderData {

    override fun toDocument(): String = this.constructArguments()

}

private data class ComplexReturnTypeBuilderData(
    val builder: ReturnTypeBuilder,
    override val arguments: Map<String, InputType> = mapOf()
) : ReturnTypeBuilderData {

    override fun toDocument(): String = "${this.constructArguments()} ${this.builder.toDocument()}"
}

