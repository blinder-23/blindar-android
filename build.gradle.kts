import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorExtension
import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorPlugin
import guru.nidi.graphviz.attribute.Color
import guru.nidi.graphviz.attribute.Style

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android.gradle)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.google.services)
        classpath(libs.firebase.crashlytics.gradle)
        classpath(libs.vanniktech.dependency.graph.generator)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
}

apply(plugin = "com.vanniktech.dependency.graph.generator")

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

subprojects {
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}

task(name = "clean", type = Delete::class) {
    delete(layout.buildDirectory)
}

plugins.apply(DependencyGraphGeneratorPlugin::class.java)

configure<DependencyGraphGeneratorExtension> {
    generators.create("blindar") {
        include = { dependency -> dependency.moduleGroup.startsWith("blindar") }
        children = { true }
        dependencyNode = { node, dependency ->
//            println("${node.name()}, ${dependency.moduleGroup}, ${dependency.moduleName}")
            val color = when (dependency.name.split(":")[0].split(".")[1]) {
                "core" -> Color.rgb(0xABCDEF)
                "feature" -> Color.rgb(0xCDEFAB)
                "data" -> Color.rgb(0xEFABCD)
                else -> Color.WHITE
            }
            node.add(Style.FILLED, color)
        }
        this.projectNode = { node, project ->
//            println(project.displayName)
            val color = when (project.displayName.split(':')[1].removeSuffix("'")) {
                "core" -> Color.rgb(0xABCDEF)
                "feature" -> Color.rgb(0xCDEFAB)
                "data" -> Color.rgb(0xEFABCD)
                "benchmark" -> Color.rgb(0xABEFCD)
                "app" -> Color.rgb(0xEFCDAB)
                else -> Color.WHITE
            }
            node.add(Style.FILLED, color)
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions.freeCompilerArgs.addAll(
        "-P",
        "plugin:androidx.compose.compiler.plugins.kotlin:experimentalStrongSkipping=true",
    )
}