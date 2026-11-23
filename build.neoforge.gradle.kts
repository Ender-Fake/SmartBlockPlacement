import groovy.json.JsonSlurper

plugins {
    id("net.neoforged.moddev") version "2.0.147"
    id("neoforge-mutex")
    id("me.modmuss50.mod-publish-plugin") version "2.2.1"
}

val secretsFile = rootProject.file("publish-secrets.json")
val secrets: Map<*, *> = if (secretsFile.exists())
    JsonSlurper().parse(secretsFile) as Map<*, *>
else emptyMap<String, String>()

version = "${property("mod.version")}+${sc.current.version}"
base.archivesName = "${property("mod.id") as String}-neoforge"

val requiredJava = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
    sc.current.parsed >= "1.18" -> JavaVersion.VERSION_17
    sc.current.parsed >= "1.17" -> JavaVersion.VERSION_16
    else -> JavaVersion.VERSION_1_8
}

val compatibleVersions: List<String> = sc.properties.rawOrNull("mod", "mc_releases")
    ?.asList().orEmpty().map { it.toString() }

repositories {
    /**
     * Restricts dependency search of the given [groups] to the [maven URL][url],
     * improving the setup speed.
     */
    fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) { name = alias } }
        filter { groups.forEach(::includeGroup) }
    }
    strictMaven("https://www.cursemaven.com", "CurseForge", "curse.maven")
    strictMaven("https://api.modrinth.com/maven", "Modrinth", "maven.modrinth")
    maven ("https://maven.shedaniel.me/") { name = "shedaniel"}

}

dependencies {


    api("me.shedaniel.cloth:cloth-config-neoforge:${property("deps.cloth_config")}")

}

neoForge {
    version = property("deps.neo_loader") as String


    mods {
        register("physicsswapping") {
            sourceSet(sourceSets.main.get())
        }
    }

    runs {
        register("client") {
            gameDirectory = file("../../run/")
            client()
        }

        register("server") {
            gameDirectory = file("../../run/")
            server()
        }
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
        fun MutableMap<String, String>.register(key: String, value: String) {
            inputs.property(key, value)
            set(key, value)
        }

        val props = buildMap {
            register("id", sc.properties["mod.id"])
            register("name", sc.properties["mod.name"])
            register("version", sc.properties["mod.version"])
            register("minecraft", sc.properties["mod.mc_compat"])
        }

        filesMatching("META-INF/neoforge.mods.toml") { expand(props) }

        val mixinJava = "JAVA_${requiredJava.majorVersion}"
        filesMatching("*.mixins.json") { expand("java" to mixinJava) }

        exclude("fabric.mod.json", "*.ct", "*.classtweaker")
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    // Includes the license file in the built mod
    withType<Jar> {
        val name = project.property("mod.id")
        inputs.property("mod_id", name)
        from("../../LICENSE") { rename { "$it-$name" } }
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds mod jars and copies results to `build/libs/{mod version}/`"

        inputs.property("version", project.property("mod.version"))
        from(jar.flatMap { it.archiveFile }, named<Jar>("sourcesJar").flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
    }

/*    withType<Jar>().configureEach {
        destinationDirectory.set(
            rootProject.layout.projectDirectory.dir("buildVersions/${project.property("mod.version")}")
        )
    }*/


}

publishMods {

    dryRun = false
    file = tasks.jar.map { it.archiveFile.get() }
    additionalFiles.from(tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar").map { it.archiveFile.get() })

    val modVersion = property("mod.version") as String
    type = if (modVersion.contains("alpha")) ALPHA
    else if (modVersion.contains("beta")) BETA
    else STABLE

    displayName = "${property("mod.name")} ${property("mod.version")} - ${stonecutter.current.version} Neoforge"
    version = "${property("mod.version")}"
    changelog = provider { rootProject.file("CHANGELOG.md").readText() }
    modLoaders.add("neoforge")


    curseforge {
        projectId = property("publish.curseforge") as String
        accessToken = (secrets["curseforge_token"] as String?)
            ?: providers.environmentVariable("CURSEFORGE_TOKEN").orNull

        minecraftVersions.addAll(compatibleVersions)
        optional("cloth-config")
        client = true
    }

    modrinth {
        projectId = property("publish.modrinth") as String
        accessToken = (secrets["modrinth_token"] as String?)
            ?: providers.environmentVariable("MODRINTH_TOKEN").orNull
        minecraftVersions.addAll(compatibleVersions)
        optional("cloth-config")
        environment=CLIENT_ONLY

    }

/*    github {
        accessToken = (secrets["github_token"] as String?)
            ?: providers.environmentVariable("GITHUB_TOKEN").orNull
        repository = property("publish.github") as String
        commitish = "stonecutter"

    }*/

}