import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.practice.firebase"
    compileSdkPreview = "VanillaIceCream"

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    testOptions {
        managedDevices {
            devices {
                maybeCreate<com.android.build.api.dsl.ManagedVirtualDevice>("pixel2api30atd").apply {
                    device = "Pixel 2"
                    apiLevel = 30
                    systemImageSource = "aosp-atd"
                }
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}

dependencies {
    // Firebase
    api(platform(libs.firebase.bom))
    api(libs.firebase.auth.ktx)
    api(libs.firebase.database)

    // androidx credential manager
    api(libs.bundles.androidx.credentials)

    // KTX libraries
    implementation(libs.androidx.core.ktx)
    // AndroidX lifecycles
    implementation(libs.bundles.lifecycle)

    // Compose Activity
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.google.play.auth)

    // Instrumented Test
    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.assertj.core)
}