pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net")
        }
    }

    plugins {
        val kotlin_version: String by settings
        val fabric_loom_version: String by settings

        kotlin("jvm") version kotlin_version
        id("fabric-loom") version fabric_loom_version
    }
}

include("plugin")