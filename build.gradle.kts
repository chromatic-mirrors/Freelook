plugins {
    java
    id("net.fabricmc.fabric-loom") version ("1.15-SNAPSHOT")
}

group = "org.codeberg.chromatic"
version = "2.0.0-alpha.1"

repositories {
    maven("https://maven.isxander.dev/releases")
    maven("https://maven.terraformersmc.com")
}

dependencies {
    minecraft("com.mojang:minecraft:26.1-snapshot-2")
    implementation("net.fabricmc:fabric-loader:0.18.4")

    implementation("dev.isxander:yet-another-config-lib:3.8.2+26.1.0-fabric")
    implementation("com.terraformersmc:modmenu:18.0.0-alpha.4")
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}