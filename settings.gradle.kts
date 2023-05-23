pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "blinder"
include(":app")
include(":database")
include(":preferences")
include(":domain")
include(":benchmark")
include(":server")
include(":work")
