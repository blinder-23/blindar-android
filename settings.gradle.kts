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
include(":benchmark")
include(":core:preferences")
include(":core:work")
include(":core:api")
include(":data:meal")
include(":data:schedule")
include(":core:date")
include(":core:combine")
include(":core:designsystem")
