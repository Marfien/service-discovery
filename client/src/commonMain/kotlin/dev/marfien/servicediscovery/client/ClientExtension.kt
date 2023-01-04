package dev.marfien.servicediscovery.client

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.api.Subscription
import kotlinx.coroutines.flow.Flow

private lateinit var client: ApolloClient

fun initSDClient(block: (ApolloClient.Builder) -> Unit) {
    client = ApolloClient.Builder()
        .apply(block)
        .build()
}

private fun <T> withClient(block: (ApolloClient) -> T): T {
    if (::client.isInitialized) return block(client)

    throw IllegalStateException("Client is not initialized. Call `initSDClient( (ApolloClient.Builder) -> Unit )` first")
}

fun <T : Query.Data> Query<T>.call(): ApolloCall<T> =
    withClient { it.query(this) }

suspend fun <T : Query.Data> Query<T>.execute(): ApolloResponse<T> =
    this.call().execute()

fun <T : Mutation.Data> Mutation<T>.call(): ApolloCall<T> =
    withClient { it.mutation(this) }

suspend fun <T : Mutation.Data> Mutation<T>.execute(): ApolloResponse<T> =
    this.call().execute()

fun <T : Subscription.Data> Subscription<T>.call(): ApolloCall<T> =
    withClient { it.subscription(this) }
fun <T : Subscription.Data> Subscription<T>.subscribe(): Flow<ApolloResponse<T>> =
    this.call().toFlow()
suspend fun <T : Subscription.Data> Subscription<T>.subscribe(subscriber: (ApolloResponse<T>) -> Unit): Unit =
    this.call().toFlow().collect { subscriber(it) }