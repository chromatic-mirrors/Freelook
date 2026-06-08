plugins {
    id("dev.kikugie.loom-back-compat")
    id("me.modmuss50.mod-publish-plugin") version "2.0.0"
}

version = "${property("mod.version")}+mc${sc.current.version}"
base.archivesName = property("mod.id") as String

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
    sc.current.parsed >= "1.18" -> JavaVersion.VERSION_17
    sc.current.parsed >= "1.17" -> JavaVersion.VERSION_16
    else -> JavaVersion.VERSION_1_8
}

val compatibleVersions: List<String> = sc.properties.rawOrNull("mod", "mc_releases")
    ?.asList().orEmpty().map { it.toString() }

repositories {
    fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) { name = alias } }
        filter { groups.forEach(::includeGroup) }
    }
    strictMaven("https://www.cursemaven.com", "CurseForge", "curse.maven")
    strictMaven("https://api.modrinth.com/maven", "Modrinth", "maven.modrinth")

    maven("https://maven.isxander.dev/releases")
    maven("https://maven.terraformersmc.com/")

    maven("https://maven.parchmentmc.org")
    maven("https://repo.polyfrost.org/releases")
    maven("https://repo.polyfrost.org/snapshots")
    maven("https://maven.gegy.dev/releases")
    maven("https://maven.logix.dev/snapshots")
    maven("https://nexus.prsm.wtf/repository/maven-public/maven-repo/releases/")
    maven("https://repo.hypixel.net/repository/Hypixel/")
    maven("https://maven.deftu.dev/releases")
    maven("https://maven.fabricmc.net/releases")
    maven("https://jitpack.io") { content { includeGroupAndSubgroups("com.github") } }
    maven("https://maven.bawnorton.com/releases") { content { includeGroup("com.github.bawnorton.mixinsquared") } }
    maven("https://maven.azureaaron.net/releases") { content { includeGroup("net.azureaaron") } }
    maven("https://redirector.kotlinlang.org/maven/compose-dev")
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    loomx.applyMojangMappings()

    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")

    modImplementation("org.polyfrost.oneconfig:${sc.current.version}-fabric:1.0.0-beta.1")
    implementation("org.polyfrost.oneconfig:commands:1.0.0-beta.1")
    implementation("org.polyfrost.oneconfig:config:1.0.0-beta.1")
    implementation("org.polyfrost.oneconfig:config-impl:1.0.0-beta.1")
    implementation("org.polyfrost.oneconfig:events:1.0.0-beta.1")
    implementation("org.polyfrost.oneconfig:internal:1.0.0-beta.1")
    implementation("org.polyfrost.oneconfig:ui:1.0.0-beta.1")
    implementation("org.polyfrost.oneconfig:utils:1.0.0-beta.1")
    implementation("org.polyfrost.oneconfig:hud:1.0.0-beta.1")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${sc.properties["deps.fabric_api"] as String}")
}

loom {
    fabricModJsonPath = rootProject.file("src/main/resources/fabric.mod.json")

    decompilerOptions.named("vineflower") {
        options.put("mark-corresponding-synthetics", "1")
    }

    runConfigs.all {
        vmArgs("-Dmixin.debug.export=true")
        runDir = "../../run"
    }
}

java {
    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava

    toolchain {
        vendor = JvmVendorSpec.ADOPTIUM
        languageVersion = JavaLanguageVersion.of(requiredJava.majorVersion)
    }
}

tasks {
    processResources {
        fun MutableMap<String, String>.register(key: String, property: String) {
            val value: String = sc.properties[property]
            inputs.property(key, value)
            set(key, value)
        }

        val props = buildMap {
            register("id", "mod.id")
            register("name", "mod.name")
            register("version", "mod.version")
            register("minecraft", "mod.mc_compat")
            register("yacl3", "deps.yacl3")
        }

        filesMatching("fabric.mod.json") { expand(props) }

        val mixinJava = "JAVA_${requiredJava.majorVersion}"
        filesMatching("*.mixins.json") { expand("java" to mixinJava) }
    }

    register<Copy>("buildAndCollect") {
        group = "build"

        from(loomx.modJar.map { it.archiveFile }, loomx.modSourcesJar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

publishMods {
    file = loomx.modJar.get().archiveFile
    changelog = project.rootProject.file("CHANGELOG.md").takeIf { it.exists() }?.readText() ?: "No changelog provided."
    type = BETA
    modLoaders.add("fabric")

    modrinth {
        projectId = property("publish.modrinth").toString()
        accessToken = findProperty("modrinth.token").toString()

        minecraftVersions.add(sc.current.version)

        requires("oneconfig")
    }
}