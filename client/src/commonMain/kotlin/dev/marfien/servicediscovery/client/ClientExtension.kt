package dev.marfien.servicediscovery.client

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.api.Subscription

private val client = ApolloClient.Builder()
    .serverUrl("server-url") // TODO
    .build()

fun <T : Query.Data> Query<T>.call() = client.query(this)

suspend fun <T : Query.Data> Query<T>.execute() = this.call().execute()

fun <T : Mutation.Data> Mutation<T>.call() = client.mutation(this)

suspend fun <T : Mutation.Data> Mutation<T>.execute() = this.call().execute()

fun <T : Subscription.Data> Subscription<T>.call() = client.subscription(this)
fun <T : Subscription.Data> Subscription<T>.subscribe() = this.call().toFlow()
suspend fun <T : Subscription.Data> Subscription<T>.subscribe(subscriber: (ApolloResponse<T>) -> Unit) = this.call().toFlow().collect { subscriber(it) }