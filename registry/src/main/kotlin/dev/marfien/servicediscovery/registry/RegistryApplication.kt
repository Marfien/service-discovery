package dev.marfien.servicediscovery.registry

import com.expediagroup.graphql.server.spring.subscriptions.ApolloSubscriptionHooks
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@SpringBootApplication
class RegistryApplication

fun main(args: Array<String>) {
    runApplication<RegistryApplication>(*args)
}