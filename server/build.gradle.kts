plugins {
    id("java")
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "app"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.14.2")
    implementation(project(":common"))
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("app.server.ServerMain")
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveBaseName.set("server")
        archiveVersion.set("1.0")
        archiveClassifier.set("")
    }
}