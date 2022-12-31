package dev.marfien.servicediscovery.client.client

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Query

private val client = ApolloClient.Builder()
    .serverUrl("server-url") // TODO
    .build()

fun <T : Query.Data> Query<T>.call() = client.query(this)

suspend fun <T : Query.Data> Query<T>.execute() = this.call().execute()
