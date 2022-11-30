plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies{
    implementation(libs.kotlin.stdlib)

    // Unit test
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
}