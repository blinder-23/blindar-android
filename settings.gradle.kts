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
rootProject.name = "blindar"
include(":app")
include(":database")
include(":preferences")
include(":domain")
include(":benchmark")
include(":server")
include(":work")
