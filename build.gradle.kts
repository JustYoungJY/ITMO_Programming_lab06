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
    // Основная зависимость Jackson для сериализации/десериализации
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    // Модуль для работы с XML
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.14.2")
    // Библиотека для поддержки Java Time API (ZonedDateTime и др.)
    implementation("com.fatboyindustrial.gson-javatime-serialisers:gson-javatime-serialisers:1.1.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2")

    // Зависимость для Gson (используем для сериализации Request/Response по сети)
    implementation("com.google.code.gson:gson:2.10")
}

tasks.test {
    useJUnitPlatform()
}

application {
    // Для сборки серверного модуля, точка входа – ServerMain.
    mainClass.set("app.server.ServerMain")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveClassifier.set("") // чтобы итоговый jar был без "-all" и прочего
}