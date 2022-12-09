plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.domain"
    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 33

        testInstrumentationRunner = "com.example.domain.di.HiltTestRunner"
        consumerProguardFiles.add(File("consumer-rules.pro"))
        testOptions {
            unitTests {
                isReturnDefaultValues = true
            }
            managedDevices {
                devices {
                    maybeCreate<com.android.build.api.dsl.ManagedVirtualDevice>("pixel2api30atd").apply {
                        device = "Pixel 2"
                        apiLevel = 30
                        systemImageSource = "aosp-atd"
                    }
                }
                devices {
                    maybeCreate<com.android.build.api.dsl.ManagedVirtualDevice>("pixel2api30").apply {
                        device = "Pixel 2"
                        apiLevel = 30
                        systemImageSource = "aosp"
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
                        targetDevices.add(devices["pixel2api30"])
                        targetDevices.add(devices["pixel2api30atd"])
                        targetDevices.add(devices["pixel2api27"])
                    }
                }
            }
        }
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":server"))
    implementation(project(":database"))

    implementation(libs.hsk.ktx)

    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.bundles.coroutines)
    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.assertj.core)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kaptAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.android.testing)
}