import fr.xpdustry.toxopid.util.ModMetadata
import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone
import java.io.ByteArrayOutputStream

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    `maven-publish`
    alias(libs.plugins.errorprone)
    alias(libs.plugins.toxopid)
    alias(libs.plugins.indra.git)
    alias(libs.plugins.indra.publishing)
}

repositories {
    mavenCentral()
}

val metadata = ModMetadata(file("${rootProject.rootDir}/plugin.json"))
group = property("props.project-group").toString()
version = metadata.version + if (indraGit.headTag() == null) "-SNAPSHOT" else ""

toxopid {
    arcCompileVersion.set(metadata.minGameVersion)
    mindustryCompileVersion.set(metadata.minGameVersion)
}

dependencies {
    // Unit tests
    testImplementation(libs.junit)

    // Static analysis
    compileOnly(libs.jetbrains.annotations)
    annotationProcessor(libs.nullaway)
    errorprone(libs.errorprone.core)
    errorproneJavac(libs.errorprone.javac)
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
    sourceCompatibility = libs.versions.java.get()
    targetCompatibility = libs.versions.java.get()
    options.encoding = "UTF-8"

    options.errorprone {
        disableWarningsInGeneratedCode.set(true)
        disable("MissingSummary")
        if (!name.contains("test", true)) {
            check("NullAway", CheckSeverity.ERROR)
            option("NullAway:AnnotatedPackages", project.property("props.root-package").toString())
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
