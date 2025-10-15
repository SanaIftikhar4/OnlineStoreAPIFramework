import java.nio.file.Files
import java.nio.file.Paths


plugins {
    id("java")
    id("io.qameta.allure") version "2.11.2"


}
allure {
    version = "2.26.0"
    autoconfigure = true
    aspectjweaver = true
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

    // Rest-Assured – main library for API testing (HTTP requests, validation, etc.)
    implementation("io.rest-assured:rest-assured:5.5.0")

    // TestNG – testing framework for running and organizing test cases
    testImplementation("org.testng:testng:7.10.2")


    // ------------------------------------------------------
    //  JSON Handling & Validation
    // ------------------------------------------------------

    // JSONPath – used internally by RestAssured to extract data from JSON
    implementation("io.rest-assured:json-path:5.5.0")

    // JSON Schema Validator – helps validate API response structure
    implementation("io.rest-assured:json-schema-validator:5.5.0")

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

    // OR — if  using the classic Java Faker library:
    //implementation("com.github.javafaker:javafaker:1.0.2")


    // ------------------------------------------------------
    //  Reporting Libraries
    // ------------------------------------------------------

    // Extent Reports – for HTML test reports with visual charts and logs
    implementation("com.aventstack:extentreports:5.1.1")

    // Allure TestNG – integrates Allure reporting with TestNG
    implementation("io.qameta.allure:allure-testng:2.26.0")
    testImplementation("io.qameta.allure:allure-rest-assured:2.26.0")
}

        tasks.register("cleanReports") {
            doLast {
                val dirsToDelete = listOf("build", "allure-report") //KEEP allure-results for Jenkins
                //val dirsToDelete = listOf("build", "allure-results", "allure-report")
                dirsToDelete.forEach { dir ->
                    val path = Paths.get(project.projectDir.path, dir)
                    if (Files.exists(path)) {
                        var attempts = 0
                        var deleted = false
                        while (!deleted && attempts < 3) {
                            try {
                                project.delete(path)
                                deleted = true
                                println("🧹 Deleted $dir successfully.")
                            } catch (e: Exception) {
                                attempts++
                                println("⚠️ Attempt $attempts to delete $dir failed. Retrying in 2 seconds...")
                                Thread.sleep(2000)
                            }
                        }
                        if (!deleted) {
                            println("❌ Could not delete $dir after 3 attempts. It may be locked by another process.")
                        }
                    }
                }
            }
        }

tasks.test {

    dependsOn("cleanReports")  // clean first

    useTestNG {
        suites("src/test/resources/testng.xml") //  Path to TestNG suite file
    }  //  Run TestNG tests instead of JUnit


    finalizedBy("allureReport")//generates Allure report after tests.

    // Optional but recommended: better visibility in test logs

    testLogging {
        events("PASSED", "FAILED", "SKIPPED")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }

    // Optional — ensures tests always run (no Gradle caching)
    outputs.upToDateWhen { false }
}
