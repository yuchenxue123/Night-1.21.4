import kotlin.String

plugins {
    kotlin("jvm")
    id("fabric-loom")
}

val mod_name: String by project
val mod_group: String by project
val mod_version: String by project
val mod_description: String by project

description = mod_description
version = mod_version
group = mod_group

base {
    archivesName = mod_name
}

repositories {
    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")
    }
}

loom {
    accessWidenerPath = file("src/main/resources/liquidbounce.accesswidener")
}

val minecraft_version: String by project
val yarn_mappings: String by project
val loader_version: String by project
val fabric_version: String by project

configurations.register("packageImplementation") {
    configurations.include.get().extendsFrom(this)
    configurations.implementation.get().extendsFrom(this)
}

fun DependencyHandler.packageImplementation(dependency: Any) {
    add("packageImplementation", dependency)
}

val kotlin_version: String by project
val kotlinx_coroutines_version: String by project
val lwjgl_version: String by project

dependencies {
    // Minecraft
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings("net.fabricmc:yarn:$yarn_mappings:v2")

    // Fabric
    modImplementation("net.fabricmc:fabric-loader:$loader_version")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabric_version")

    // NanoVg
    packageImplementation("org.lwjgl:lwjgl-nanovg:$lwjgl_version")
    packageImplementation("org.lwjgl:lwjgl-nanovg:$lwjgl_version:natives-windows")
    packageImplementation("org.lwjgl:lwjgl-nanovg:$lwjgl_version:natives-linux")
    packageImplementation("org.lwjgl:lwjgl-nanovg:$lwjgl_version:natives-macos")

    // Kotlin
    packageImplementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    packageImplementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    packageImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinx_coroutines_version")

    // EventSystem
    packageImplementation("com.github.yuchenxue123:event-system-kotlin:1.2")
}

val properties = mapOf(
    "id" to mod_name.lowercase(),
    "name" to mod_name,
    "version" to mod_version,
    "description" to mod_description,
)

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand(properties)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"

    options.release = 21
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmToolchain(21)
    }
}




