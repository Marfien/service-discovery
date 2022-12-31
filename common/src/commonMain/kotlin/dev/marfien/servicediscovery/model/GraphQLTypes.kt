package dev.marfien.servicediscovery.model

open class GraphQLType(val name: String, val kotlinType: String)

object GraphQLString : GraphQLType("String", "kotlin.String"), InputType

object GraphQLID : GraphQLType("ID", "kotlin.String"), InputType

object GraphQLBoolean : GraphQLType("Boolean", "kotlin.Boolean"), InputType

object GraphQLFloat : GraphQLType("Float", "kotlin.Float"), InputType

object GraphQLInt : GraphQLType("Int", "kotlin.Int"), InputType