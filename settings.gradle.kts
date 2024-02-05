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
include(":data:preferences")
include(":core:work")
include(":data:api")
include(":data:meal")
include(":data:schedule")
include(":core:combine")
include(":core:designsystem")
include(":feature:main")
include(":feature:onboarding")
include(":feature:register")
include(":core:firebase")
include(":core:util")
include(":data:domain")
include(":data:memo")
include(":core:auth")
include(":core:notification")
include(":feature:settings")
