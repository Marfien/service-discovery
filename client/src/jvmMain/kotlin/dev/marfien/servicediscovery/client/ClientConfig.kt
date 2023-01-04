package dev.marfien.servicediscovery.client

actual object ClientConfig {
    actual val serverUrl: String
        get() = System.getenv("GRAPHQL_URL")
    actual val webSocketUrl: String
        get() = System.getenv("GRAPHQL_WS_URL")

}