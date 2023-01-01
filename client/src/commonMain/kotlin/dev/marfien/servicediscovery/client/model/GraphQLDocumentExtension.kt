package dev.marfien.servicediscovery.client.model

import com.apollographql.apollo3.api.*
import dev.marfien.servicediscovery.model.GraphQLType

fun CompiledSelection.toDocument(): String = when (this) {
    is CompiledField ->
        "$name${constructArguments()}${
            constructFields(this.selections)
        }"

    is CompiledFragment ->
        "...on $typeCondition${
            constructFields(this.selections)
        }"
}

fun constructFields(selections: List<CompiledSelection>): String = selections.takeUnless { it.isEmpty() }?.let {
    it.joinToString(
        prefix = "{ ",
        postfix = " }",
        separator = " "
    ) { sec -> sec.toDocument() }
} ?: ""

private fun CompiledField.constructArguments(): String = this.arguments.takeUnless { it.isEmpty() }?.let {
    it.joinToString(
        separator = " ",
        prefix = "(",
        postfix = ")"
    ) { arg -> arg.toDocument() }
} ?: ""

fun CompiledType.toDocumentType(): String = when (this) {
    is CompiledNotNullType -> "${this.ofType.toDocumentType()}!"
    is CompiledListType -> "[${this.ofType.toDocumentType()}]"
    is CompiledNamedType -> this.name
}

fun CompiledArgument.toDocument(): String = "$name: ${this.value.mapToValue()}"

private fun Any?.mapToValue(): String = when (this) {
    is String -> "\"$this\""
    is Number, Boolean, null -> this.toString()
    is List<*> -> this.joinToString(prefix = "[", postfix = "]") { it.mapToValue() }
    is Map<*, *> -> this.entries.joinToString(prefix = "{ ", postfix = " }") { (k, v) -> "$k: ${v.mapToValue()}" }
    is CompiledVariable -> "$$name"
    else -> throw IllegalStateException("Cannot map to value: ${this::class}")
}