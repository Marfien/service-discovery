plugins {
    val kotlinVersion = "1.7.21"

    kotlin("jvm") version kotlinVersion apply false
    kotlin("plugin.spring") version kotlinVersion apply false
    kotlin("multiplatform") version kotlinVersion apply false
}

group = "dev.marfien"
version = "1.0-SNAPSHOT"

extra["javaVersion"] = JavaVersion.VERSION_17

allprojects {
    group = "${this.rootProject.group}.servicediscovery"
    version = version

    repositories {
        mavenCentral()
        mavenLocal()
    }
}