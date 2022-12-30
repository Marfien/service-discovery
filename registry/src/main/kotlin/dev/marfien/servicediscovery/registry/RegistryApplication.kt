package dev.marfien.servicediscovery.registry

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class RegistryApplication

fun main(args: Array<String>) {
    SpringApplication.run(RegistryApplication::class.java)
}