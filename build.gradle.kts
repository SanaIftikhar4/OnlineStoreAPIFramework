import java.nio.file.Files
import java.nio.file.Paths
import java.awt.Desktop
import java.io.File


plugins {

    id("java")
    id("io.qameta.allure") version "2.11.2"


}


allure {
    version.set("2.29.0")          //  Stable version compatible with plugin 2.11.2
    adapter.autoconfigure.set(true)
    adapter.aspectjWeaver.set(true)
}


group = "com.api"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    // ------------------------------------------------------
    //  Core Testing Libraries
    // ------------------------------------------------------


    implementation("io.rest-assured:rest-assured:5.5.0")

    // TestNG – testing framework for running and organizing test cases
    testImplementation("org.testng:testng:7.10.2")


    // ------------------------------------------------------
    //  Rest-Assured
    // ------------------------------------------------------

    // Rest-Assured – main library for API testing (HTTP requests, validation, etc.)
    implementation("io.rest-assured:rest-assured:5.5.0")
    // JSONPath – used internally by RestAssured to extract data from JSON
    implementation("io.rest-assured:json-path:5.5.0")

    // JSON Schema Validator – helps validate API response structure
    implementation("io.rest-assured:json-schema-validator:5.5.0")


    // ------------------------------------------------------
    //  JSON handling
    // ------------------------------------------------------
    // Jackson Databind – powerful JSON serialization/deserialization library (used with POJOs)
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")

    // org.json – lightweight JSON parsing (optional but useful for manual validation)
    implementation("org.json:json:20240303")


    // ------------------------------------------------------
    //  Dynamic Test Data Generation
    // ------------------------------------------------------

    // DataFaker – generates realistic random data (names, prices, categories, etc.)
    // use this if upgraded to net.datafaker (recommended modern version)
    implementation("net.datafaker:datafaker:2.4.1")


    // ------------------------------------------------------
    //  Reporting Libraries
    // ------------------------------------------------------

    // Extent Reports – for HTML test reports with visual charts and logs
    implementation("com.aventstack:extentreports:5.1.1")

    // Allure Java adapters Allure TestNG – integrates Allure reporting with TestNG
    implementation("io.qameta.allure:allure-testng:2.29.1")
    testImplementation("io.qameta.allure:allure-rest-assured:2.29.1")


}


tasks.test {
    useTestNG {
        suites("src/test/resources/testng.xml")
        listeners.add("io.qameta.allure.testng.AllureTestNg")
        listeners.add("utils.ExtentReporter")
    }

    testLogging {
        events("PASSED", "FAILED", "SKIPPED")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStandardStreams = true
    }

    outputs.upToDateWhen { false }
    finalizedBy("allureReport")
}
tasks.register("forceClean") {
    doLast {
        val buildDirPath = project.layout.buildDirectory.asFile.get().toPath()
        if (Files.exists(buildDirPath)) {
            try {
                project.delete(buildDirPath)
                println("🧹 Build directory deleted successfully.")
            } catch (e: Exception) {
                println("⚠️ Could not delete build directory: ${e.message}")
            }
        }
    }
}
tasks.named("allureReport") {
    doFirst {
        println("🧹 Cleaning previous Allure reports...")
        delete("${project.buildDir}/reports/allure-report/allureReport")
    }
    doLast {
        val reportPath = "${project.buildDir}/reports/allure-report/allureReport/index.html"
        println("📊 Allure report generated successfully at:")
        println("👉 file://$reportPath")
        println("💡 Open the link above manually in your browser.")
    }
}