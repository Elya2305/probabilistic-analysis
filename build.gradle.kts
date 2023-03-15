import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:3.0.2")
    implementation("org.jetbrains.lets-plot:lets-plot-image-export:2.1.0")
    implementation(kotlin("stdlib-jdk8"))
}

java.sourceCompatibility = JavaVersion.VERSION_17

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}
