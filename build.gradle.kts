import fr.xpdustry.toxopid.util.ModMetadata
import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone
import java.io.ByteArrayOutputStream

plugins {
    java
    `maven-publish`
    id("net.ltgt.errorprone") version "2.0.2"
    id("net.kyori.indra.publishing") version "2.1.1"
    id("net.kyori.indra.git") version "2.1.1"
    id("fr.xpdustry.toxopid") version "1.3.0"
}

repositories {
    mavenCentral()
}

object Versions {
    const val mindustry     = "v126.2"
    const val arc           = "v126.2"
    const val junit         = "5.8.2"
}

val metadata = ModMetadata(file("${rootProject.rootDir}/plugin.json"))
group = property("props.project-group").toString()
version = metadata.version + if (indraGit.headTag() == null) "-SNAPSHOT" else ""

toxopid {
    arcCompileVersion.set(Versions.arc)
    mindustryCompileVersion.set(Versions.mindustry)
}

dependencies {
    // Unit tests
    testImplementation("org.junit.jupiter:junit-jupiter-params:${Versions.junit}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.junit}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Versions.junit}")

    // Static analysis
    compileOnly("org.jetbrains:annotations:23.0.0")
    annotationProcessor("com.uber.nullaway:nullaway:0.9.4")
    errorprone("com.google.errorprone:error_prone_core:2.10.0")
    errorproneJavac("com.google.errorprone:javac:9+181-r4173-1")
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.javadoc {
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType(JavaCompile::class.java).configureEach {
    sourceCompatibility = "17"
    targetCompatibility = "17"
    options.encoding = "UTF-8"

    options.errorprone {
        disableWarningsInGeneratedCode.set(true)
        disable("MissingSummary")
        if (!name.contains("test", true)) {
            check("NullAway", CheckSeverity.ERROR)
            option("NullAway:AnnotatedPackages", "fr.xpdustry.template")
        }
    }
}

// Disables the signing task
tasks.signMavenPublication.get().enabled = false

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

indra {
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
        from(components["java"])

        pom {
            developers {
                developer { id.set(metadata.author) }
            }
        }
    }
}
