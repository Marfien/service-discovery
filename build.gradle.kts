plugins {
    val kotlinVersion = "1.7.21"

    kotlin("jvm") version kotlinVersion apply false
    kotlin("plugin.spring") version kotlinVersion apply false
    kotlin("multiplatform") version kotlinVersion apply false
}

group = "dev.marfien"
version = "1.0-SNAPSHOT"

allprojects {
    this.group = "${this.group}.servicediscovery"
    this.version = this.version

    repositories {
        mavenCentral()
        mavenLocal()
    }

    this.extra["javaVersion"] = JavaVersion.VERSION_17
}