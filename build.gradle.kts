import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
}

group = "com.pkulak.memo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val exposedVersion: String by project
val koinVersion: String by project

dependencies {
    // Utils
    implementation("com.google.guava:guava:30.1.1-jre")
    implementation("uk.oczadly.karl:jnano:2.20.1")

    // Dependency Injection
    implementation("io.insert-koin:koin-core:$koinVersion")

    // Storage
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.postgresql:postgresql:42.2.20")

    // Logging
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("io.github.microutils:kotlin-logging:2.0.8")

    // Testing
    testImplementation(platform("org.junit:junit-bom:5.7.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.kotest:kotest-assertions-jvm:4.0.7")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}