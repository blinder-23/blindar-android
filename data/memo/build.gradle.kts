plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.practice.memo"
    compileSdk = 33

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "com.practice.memo.testutil.HiltTestRunner"
        consumerProguardFiles("consumer-rules.pro")
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
            devices {
                maybeCreate<com.android.build.api.dsl.ManagedVirtualDevice>("pixel2api27").apply {
                    device = "Pixel 2"
                    apiLevel = 27
                    systemImageSource = "aosp"
                }
            }
            groups {
                maybeCreate("api27and30").apply {
                    targetDevices.add(devices["pixel2api30atd"])
                    targetDevices.add(devices["pixel2api27"])
                }
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core:domain"))

    // Kotlin Coroutines
    implementation(libs.bundles.coroutines)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)
    testImplementation(libs.room.testing)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kaptAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.android.testing)

    // Unit test
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.vintage.engine)
    testImplementation(libs.assertj.core)
    testImplementation(libs.kotlinx.coroutines.test)

    // Android test
    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.assertj.core)
}