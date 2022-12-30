package dev.marfien.servicediscovery.model

open class GraphQLType(val name: String, val kotlinType: String)

object GraphQLString : GraphQLType("String", "kotlin.String")

object GraphQLID : GraphQLType("ID", "kotlin.String")

object GraphQLBoolean : GraphQLType("Boolean", "kotlin.Boolean")

class GraphQLFloat : GraphQLType("Float", "kotlin.Float")

class GraphQLInt : GraphQLType("Int", "kotlin.Int")