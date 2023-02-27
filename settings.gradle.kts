pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositories {
        mavenLocal()
    }
    versionCatalogs {
        create("libs") {
            from("kr.hsk:gradle-version-catalog:1.0.1")
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
