import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.practice.user"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    api(project(":core:firebase"))
    api(project(":data:preferences"))

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.compiler.androidx)
    androidTestImplementation(libs.hilt.android.testing)
    implementation(libs.hilt.work)
}