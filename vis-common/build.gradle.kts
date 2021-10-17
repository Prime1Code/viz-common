import org.gradle.api.JavaVersion.VERSION_11
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.gradle.api.publish.maven.MavenPublication as MavenPublication1

plugins {
    `java-library`
    //id("org.jetbrains.kotlin.jvm") version "1.5.0"
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.spring") version "1.5.21"

    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    id("maven-publish")
}

group = "com.bestappsintown"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = VERSION_11
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    //implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.jar {
    enabled = true
    manifest {
        attributes(mapOf("Implementation-Title" to project.name,
            "Implementation-Version" to project.version))
    }
}

tasks.named<BootJar>("bootJar") {
    enabled = false
}

publishing {
    publications {
        create<MavenPublication1>("mavenJava") {
            from(components["java"])
        }
    }
}