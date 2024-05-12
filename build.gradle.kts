import fr.xpdustry.toxopid.dsl.mindustryDependencies
import fr.xpdustry.toxopid.spec.ModMetadata
import fr.xpdustry.toxopid.spec.ModPlatform

plugins {
    id("net.kyori.indra") version "3.1.3"
    id("net.kyori.indra.git") version "3.1.3"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("fr.xpdustry.toxopid") version "3.2.0"
}

val metadata = ModMetadata.fromJson(rootProject.file("plugin.json"))

group = "com.xpdustry"
val rootPackage = "com.xpdustry.template"
version = metadata.version
description = metadata.description

toxopid {
    compileVersion.set("v${metadata.minGameVersion}")
    platforms.set(setOf(ModPlatform.HEADLESS))
}

repositories {
    mavenCentral()
    // This repository provides mindustry artifacts built by xpdustry
    maven("https://maven.xpdustry.com/mindustry")
    // This repository provides xpdustry libraries, such as the distributor-api
    maven("https://maven.xpdustry.com/releases")
}

dependencies {
    mindustryDependencies()
}

// Indra will set up a lot of boilerplate for you, only leaving the important parts to configure
indra {
    javaVersions {
        target(17)
        minimumToolchain(17)
    }

    // The license of your project, kyori has already functions for the most common licenses
    // such as gpl3OnlyLicense() for GPLv3, apache2License() for Apache 2.0, etc.
    // You can still specify your own license using the license { } builder function.
    mitLicense()

    if (metadata.repo.isNotBlank()) {
        val repo = metadata.repo.split("/")
        github(repo[0], repo[1]) {
            ci(true)
            issues(true)
            scm(true)
        }
    }

    configurePublications {
        pom {
            developers {
                developer {
                    id.set(metadata.author)
                }
            }
        }
    }
}

// Required for the GitHub actions
tasks.register("getArtifactPath") {
    doLast { println(tasks.shadowJar.get().archiveFile.get().toString()) }
}

tasks.shadowJar {
    // Makes sure the name of the final jar is (plugin-name).jar
    archiveFileName.set("${metadata.name}.jar")
    // Set the classifier to "plugin" since it's the final artifact
    archiveClassifier.set("plugin")
    // Configure the dependencies shading.
    // WARNING: SQL drivers do not play well with shading,
    // the best solution would be to load them in an isolated classloader.
    // If it's too difficult, you can disable relocation but be aware this can conflict with other plugins.
    isEnableRelocation = true
    relocationPrefix = "$rootPackage.shadow"
    // Reduce shadow jar size by removing unused classes.
    // Warning, if one of your dependencies use service loaders or reflection, add it to the exclude list
    // such as "minimize { exclude(dependency("some.group:some-dependency:.*")) }"
    minimize()
    // Include the plugin.json file with the modified version
    from(rootProject.file("plugin.json"))
    // Include the license of your project
    from(rootProject.file("LICENSE.md")) {
        into("META-INF")
    }
}

tasks.build {
    // Make sure the shadow jar is built during the build task
    dependsOn(tasks.shadowJar)
}

tasks.runMindustryClient {
    // Little quirk of toxopid, it always includes the final jar in the mod list.
    // But we are building a plugin, not a mod. So we need to clear the mod list.
    mods.setFrom()
}
