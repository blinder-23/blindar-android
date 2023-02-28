import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

val url: String = gradleLocalProperties(rootDir).getProperty("server.url")

android {
    namespace = "com.example.server"
    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 33

        testInstrumentationRunner = "com.example.server.di.HiltTestRunner"
        consumerProguardFiles.add(File("consumer-rules.pro"))
        buildConfigField("String", "SERVER_URL", url)
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.hsk.ktx)

    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)

    implementation(libs.bundles.retrofit)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)

    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.assertj.core)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.compiler.androidx)
    kaptAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.android.testing)
}