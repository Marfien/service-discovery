package dev.marfien.servicediscovery.registry.scalar

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.graphql.dgs.DgsScalar
import graphql.schema.Coercing
import graphql.schema.CoercingSerializeException

@DgsScalar(name = "Dict")
class DictScalar(
    private val mapper: ObjectMapper
) : Coercing<Map<String, Any>, Any> {

    override fun serialize(dataFetcherResult: Any): Map<*, *> = when (dataFetcherResult) {
        is Map<*, *> -> dataFetcherResult
        in arrayOf("{}", "") -> mapOf<String, Any>()
        is String -> this.mapper.convertValue(dataFetcherResult, Map::class.java)
        else -> throw CoercingSerializeException("Not a Map: ${dataFetcherResult.javaClass}")
    }

    override fun parseValue(input: Any): Map<String, Any> = when (input) {
        is String, is JsonNode -> this.mapper.convertValue(input, object : TypeReference<Map<String, Any>>() {})
        else -> throw CoercingSerializeException("Input not a String")
    }

    override fun parseLiteral(input: Any): Map<String, Any> = this.parseValue(input)
}