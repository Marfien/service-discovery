package dev.marfien.servicediscovery.registry

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RegistryApplication

fun main(args: Array<String>) {
    checkEnvironmentVariables()
    runApplication<RegistryApplication>(*args)
}

private fun checkEnvironmentVariables() {
    if (System.getenv("MONGODB_URI") == null)
        throw IllegalStateException("Cannot find 'MONGODB_URI' environment variable.")
}
