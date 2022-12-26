pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = java.net.URI.create("https://jitpack.io") }
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle-build-scripts/libs.versions.toml"))
        }
    }
}
rootProject.name = "Hanbit lunch"
include(":app")
include(":database")
include(":preferences")
include(":domain")
include(":benchmark")
include(":server")
include(":work")
