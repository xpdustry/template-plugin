import java.util.*

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://repo.xpdustry.fr/releases") }
    }
}

val props = Properties()
file("./gradle.properties").reader().use { props.load(it) }
rootProject.name = props.getProperty("props.project-name")
