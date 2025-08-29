plugins {
    kotlin("jvm")
}

group = "cute.neko.night"
version = "0.1"

val kotlin_version: String by rootProject

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
}

kotlin {
    compilerOptions {
        jvmToolchain(21)
    }
}