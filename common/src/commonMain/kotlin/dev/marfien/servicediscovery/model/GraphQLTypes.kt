package dev.marfien.servicediscovery.model

open class GraphQLType(val name: String, val kotlinType: String)

object GraphQLString : GraphQLType("String", "kotlin.String"), InputType

object GraphQLID : GraphQLType("ID", "kotlin.String"), InputType

object GraphQLBoolean : GraphQLType("Boolean", "kotlin.Boolean"), InputType

class GraphQLFloat : GraphQLType("Float", "kotlin.Float"), InputType

class GraphQLInt : GraphQLType("Int", "kotlin.Int"), InputType