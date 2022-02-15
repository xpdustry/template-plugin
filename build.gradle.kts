import fr.xpdustry.toxopid.util.ModMetadata
import fr.xpdustry.toxopid.extension.ModTarget
import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone
import java.io.ByteArrayOutputStream

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    jacoco
    `maven-publish`
    alias(libs.plugins.errorprone)
    alias(libs.plugins.toxopid)
    alias(libs.plugins.versions)
    alias(libs.plugins.indra)
    alias(libs.plugins.indra.publishing)
}

val metadata = ModMetadata(file("${rootProject.rootDir}/plugin.json"))
group = property("props.project-group").toString()
version = metadata.version + if (indraGit.headTag() == null) "-SNAPSHOT" else ""

toxopid {
    modTarget.set(ModTarget.HEADLESS)
    arcCompileVersion.set(metadata.minGameVersion)
    mindustryCompileVersion.set(metadata.minGameVersion)
}

repositories {
    mavenCentral()
}

dependencies {
    // Unit tests
    testImplementation(libs.junit)

    // Static analysis
    compileOnly(libs.jetbrains.annotations)
    annotationProcessor(libs.nullaway)
    errorprone(libs.errorprone.core)
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

// Required if you want to use the Release GitHub action
tasks.create("getArtifactPath") {
    doLast { println(tasks.shadowJar.get().archiveFile.get().toString()) }
}

tasks.create("createRelease") {
    dependsOn("requireClean")

    doLast {
        // Checks if a signing key is present
        val signing = ByteArrayOutputStream().use { out ->
            exec {
                commandLine("git", "config", "--global", "user.signingkey")
                standardOutput = out
            }.run { exitValue == 0 && out.toString().isNotBlank() }
        }

        exec {
            commandLine(arrayListOf("git", "tag", "v${metadata.version}", "-F", "./CHANGELOG.md", "-a").apply { if (signing) add("-s") })
        }

        exec {
            commandLine("git", "push", "origin", "--tags")
        }
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(false)
        csv.required.set(false)
    }
}

indra {
    javaVersions {
        val version = libs.versions.java.get().toInt()
        target(version)
        minimumToolchain(version)
    }

    publishReleasesTo("xpdustry", "https://repo.xpdustry.fr/releases")
    publishSnapshotsTo("xpdustry", "https://repo.xpdustry.fr/snapshots")

    mitLicense()

    if (metadata.repo != null) {
        val repo = metadata.repo!!.split("/")
        github(repo[0], repo[1]) {
            ci(true)
            issues(true)
            scm(true)
        }
    }

    configurePublications {
        pom {
            developers {
                developer { id.set(metadata.author) }
            }
        }
    }
}
