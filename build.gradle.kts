plugins {
    id("java")
    application
}

group = "app"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Основная зависимость Jackson для сериализации/десериализации
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    // Модуль для работы с XML
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.14.2")
    // Библиотека для поддержки Java Time API (ZonedDateTime и др.)
    implementation("com.fatboyindustrial.gson-javatime-serialisers:gson-javatime-serialisers:1.1.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("app.Main")
}