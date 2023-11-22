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
include(":core:combine")
include(":core:designsystem")
include(":feature:main")
include(":feature:onboarding")
include(":feature:register")
include(":core:firebase")
include(":core:util")
include(":core:domain")
include(":data:memo")
