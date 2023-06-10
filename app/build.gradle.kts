import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.practice.hanbitlunch"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.3.1"
        signingConfig = signingConfigs.getByName("debug")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        animationsDisabled = true
        unitTests.all {
            testCoverage {
                it.excludes.add("jdk.internal.*")
            }
        }
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
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
    buildTypes {
        release {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.add("release")
            isDebuggable = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packagingOptions {
        resources {
            excludes.addAll(
                arrayOf(
                    "/META-INF/{AL2.0,LGPL2.1}",
                    "META-INF/AL2.0",
                    "META-INF/LGPL2.1",
                )
            )
        }
    }
    namespace = "com.practice.hanbitlunch"
}

dependencies {
    // mwy3055 library
    implementation(libs.hsk.ktx)
    implementation(libs.violet.dreams.core)
    implementation(libs.violet.dreams.ui)

    implementation(project(":feature:main"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:login"))
    implementation(project(":feature:register"))

    // KTX libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.collection.ktx)
    implementation(libs.androidx.palette.ktx)

    // AndroidX lifecycles
    implementation(libs.bundles.lifecycle)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    // AndroidX WorkManager
    implementation(libs.androidx.work.runtime.ktx)
    androidTestImplementation(libs.androidx.work.testing)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.compiler.androidx)
    androidTestImplementation(libs.hilt.android.testing)
    implementation(libs.hilt.work)

    // Other Jetpack Libraries
    implementation(libs.bundles.jetpack)

    // Unit Test
//    testImplementation(libs.junit.jupiter)
//    testRuntimeOnly(libs.junit.vintage.engine)
//    testImplementation(libs.assertj.core)
//    androidTestUtil(libs.androidx.test.orchestrator)

    // Instrumented Test
//    androidTestImplementation(libs.bundles.android.test)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.google.play.auth)

    // Accompanist
    implementation(libs.bundles.accompanist)

    // Kotlin Coroutines
    implementation(libs.bundles.coroutines)
    testImplementation(libs.kotlinx.coroutines.test)

    // Kotlin immutable collections
    implementation(libs.kotlinx.collections.immutable)
}