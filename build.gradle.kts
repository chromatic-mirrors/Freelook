plugins {
    java
    kotlin("jvm") version ("2.3.10")
    id("net.fabricmc.fabric-loom-remap") version ("1.15-SNAPSHOT")
}

group = "org.codeberg.chromatic"
version = "2.0.0-alpha.1"

repositories {
    maven("https://maven.isxander.dev/releases")
    maven("https://maven.terraformersmc.com")
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.5")
    mappings("net.fabricmc:yarn:1.21.5+build.1:v2")
    modImplementation("net.fabricmc:fabric-loader:0.18.4")
    modImplementation("dev.isxander:yet-another-config-lib:3.8.2+1.21.5-fabric")
    modImplementation("com.terraformersmc:modmenu:14.0.1")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}