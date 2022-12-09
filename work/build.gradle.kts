plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.work"
    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles.add(File("consumer-rules.pro"))
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":database"))
    implementation(project(":server"))
    implementation(project(":domain"))
    implementation(project(":preferences"))

    implementation(libs.hsk.ktx)

    implementation(libs.kotlinx.collections.immutable)

    // KTX libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.collection.ktx)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.compiler.androidx)
    kaptAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    implementation(libs.hilt.work)

    implementation(libs.androidx.work.runtime.ktx)
    androidTestImplementation(libs.androidx.work.testing)

    implementation(libs.bundles.coroutines)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.assertj.core)
    androidTestUtil(libs.androidx.test.orchestrator)
}