plugins {
    id("java")
}

group = "com.api"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //  Core libraries
    testImplementation("io.rest-assured:rest-assured:5.5.0")
    testImplementation("org.testng:testng:7.10.2")

    //  JSON parsing and validation
    testImplementation("io.rest-assured:json-path:5.5.0")
    testImplementation("io.rest-assured:json-schema-validator:5.5.0")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    testImplementation("org.json:json:20240303")

    //  Data faker for dynamic test data
    testImplementation("com.github.javafaker:javafaker:1.0.2")

    //  Reporting
    testImplementation("com.aventstack:extentreports:5.1.1")
    testImplementation("io.qameta.allure:allure-testng:2.26.0")
}

tasks.test {
    useTestNG()  // ✅ Tells Gradle to run TestNG instead of JUnit

    // Optional but helpful logging configuration
    testLogging {
        events("PASSED", "FAILED", "SKIPPED")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }

    // Optional — clear cache before running
    outputs.upToDateWhen { false }
}

