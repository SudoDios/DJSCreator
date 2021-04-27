import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id ("java")
    id ("application")
    id ("org.openjfx.javafxplugin") version "0.0.9"
    kotlin("jvm") version "1.4.32"
}

group = "com.sdidev.desk.dsc"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit"))

    implementation("com.jfoenix:jfoenix:9.0.10")
    implementation("org.hildan.fxgson:fx-gson:3.1.2")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

javafx {
    version = "15.0.1"
    modules = arrayOf("javafx.controls","javafx.base","javafx.graphics","javafx.fxml").toMutableList()
}
