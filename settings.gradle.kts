@file:Suppress("PropertyName")

pluginManagement {
    repositories {
        maven("https://maven.deftu.dev/releases")
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://jitpack.io/")

        maven("https://maven.deftu.dev/snapshots")
        mavenLocal()

        gradlePluginPortal()
        mavenCentral()
    }
}

val projectName: String = extra["mod.name"]?.toString()!!
rootProject.name = projectName
rootProject.buildFileName = "build.gradle.kts"