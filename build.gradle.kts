plugins {
    id("java")
}

group = "com.api"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // ✅ Core libraries
    testImplementation("io.rest-assured:rest-assured:5.5.0")
    testImplementation("org.testng:testng:7.10.2")

    // ✅ JSON parsing and validation
    testImplementation("io.rest-assured:json-path:5.5.0")
    testImplementation("io.rest-assured:json-schema-validator:5.5.0")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    testImplementation("org.json:json:20240303")

    // ✅ Data faker for dynamic test data
    testImplementation("com.github.javafaker:javafaker:1.0.2")

    // ✅ Reporting
    testImplementation("com.aventstack:extentreports:5.1.1")
    testImplementation("io.qameta.allure:allure-testng:2.26.0")
}

tasks.test {
    // ✅ Tell Gradle to use TestNG instead of JUnit
    useTestNG() {
        suites("src/test/resources/testng.xml")
    }
}
