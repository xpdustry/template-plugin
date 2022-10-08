import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import fr.xpdustry.toxopid.ModPlatform
import fr.xpdustry.toxopid.util.ModMetadata
import fr.xpdustry.toxopid.util.anukenJitpack
import fr.xpdustry.toxopid.util.mindustryDependencies
import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone

plugins {
    id("net.kyori.indra") version "3.0.0"
    id("net.kyori.indra.publishing") version "3.0.0"
    id("net.kyori.indra.git") version "3.0.0"
    id("net.kyori.indra.licenser.spotless") version "3.0.0"
    id("net.ltgt.errorprone") version "2.0.2"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("fr.xpdustry.toxopid") version "2.1.1"
}

val metadata = ModMetadata.fromJson(file("plugin.json").readText())
group = property("props.project-group").toString()
metadata.version = metadata.version + if (indraGit.headTag() == null) "-SNAPSHOT" else ""
version = metadata.version
description = metadata.description

toxopid {
    compileVersion.set("v" + metadata.minGameVersion)
    platforms.add(ModPlatform.HEADLESS)
}

repositories {
    mavenCentral()
    anukenJitpack()
}

dependencies {
    mindustryDependencies()

    val junit = "5.9.0"
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junit")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit")

    val jetbrains = "23.0.0"
    compileOnly("org.jetbrains:annotations:$jetbrains")
    testCompileOnly("org.jetbrains:annotations:$jetbrains")

    // Static analysis
    annotationProcessor("com.uber.nullaway:nullaway:0.10.1")
    errorprone("com.google.errorprone:error_prone_core:2.15.0")
}

tasks.withType(JavaCompile::class.java).configureEach {
    options.errorprone {
        disableWarningsInGeneratedCode.set(true)
        disable("MissingSummary")
        if (!name.contains("test", true)) {
            check("NullAway", CheckSeverity.ERROR)
            option("NullAway:AnnotatedPackages", project.property("props.root-package").toString())
        }
    }
}

// Disables the signing task, removes this line only if you know what you are doing
tasks.signMavenPublication.get().enabled = false

// Required for the GitHub actions
tasks.create("getArtifactPath") {
    doLast { println(tasks.shadowJar.get().archiveFile.get().toString()) }
}

// Relocates dependencies
val relocate = tasks.create<ConfigureShadowRelocation>("relocateShadowJar") {
    target = tasks.shadowJar.get()
    prefix = project.property("props.root-package").toString() + ".shadow"
}

tasks.shadowJar {
    // Configure the dependencies shading
    dependsOn(relocate)
    // Reduce shadow jar size by removing unused classes
    minimize()
    // Include the plugin.json file with the modified version
    doFirst {
        val temp = temporaryDir.resolve("plugin.json")
        temp.writeText(metadata.toJson(true))
        from(temp)
    }
    // Include the license of your project
    from(rootProject.file("LICENSE.md")) {
        into("META-INF")
    }
}

tasks.build.get().dependsOn(tasks.shadowJar)

indra {
    javaVersions {
        target(17)
        minimumToolchain(17)
    }

    // The license of your project, use gpl3OnlyLicense() if your project is under GPL3
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

indraSpotlessLicenser {
    licenseHeaderFile(rootProject.file("LICENSE_HEADER.md"))
    // Some properties to make updating the licence header easier
    property("NAME", metadata.displayName)
    property("DESCRIPTION", metadata.description)
    property("AUTHOR", metadata.author)
    property("YEAR", property("props.project-year").toString())
}
