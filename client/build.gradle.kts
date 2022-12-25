plugins {
    kotlin("multiplatform")
    id("com.apollographql.apollo3") version "3.7.3"
}

apollo {
    service("service") {
        packageName.set("${project.group}.client")
    }
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = project.extra["javaVersion"].toString()
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
                implementation("com.apollographql.apollo3:apollo-runtime:3.7.3")
                implementation(project(":common"))
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