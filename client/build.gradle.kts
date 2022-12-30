plugins {
    kotlin("multiplatform")
    id("com.apollographql.apollo3") version "3.7.3"
}

apollo {
    service("services") {

        schemaFile.set(
            rootProject.project("common")
                .file("src")
                .resolve("commonMain")
                .resolve("resources")
                .resolve("schema")
                .resolve("services.graphqls")
        )

        packageName.set("${project.group}.client")
    }
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = rootProject.extra["javaVersion"].toString()
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(BOTH) {
        browser()
        nodejs()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation("com.apollographql.apollo3:apollo-runtime:3.7.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
    }
}

operator fun <K : Any, V : Any> MapProperty<K, V>.set(key: K, value: V) = this.put(key, value)