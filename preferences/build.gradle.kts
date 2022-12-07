plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.practice.preferences"
    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 33

        testInstrumentationRunner = "com.practice.preferences.HiltTestRunner"
        consumerProguardFiles.add(File("consumer-rules.pro"))
    }
    testOptions {
        managedDevices {
            devices {
                maybeCreate<com.android.build.api.dsl.ManagedVirtualDevice>("pixel2api30").apply {
                    device = "Pixel 2"
                    apiLevel = 30
                    systemImageSource = "aosp-atd"
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
    implementation(libs.hsk.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.preferences)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.compiler.androidx)
    kaptAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.android.testing)

    // Unit test
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testImplementation(libs.kotlinx.coroutines.test)

    // Android test
    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.assertj.core)
}