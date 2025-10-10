//Enhanced BaseTest using AI tool
package base;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import routes.Routes;
import utils.ConfigReader;
import utils.LogHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * BaseTest:
 * Acts as the foundation of all API test classes.
 * Handles environment setup, logging, configuration, and request specifications.
 * Every test class (like ProductTests, CartTests) extends this BaseTest.
 */
public class BaseTest {

    // Loads config.properties values (base URL, contentType, etc.)
    protected static ConfigReader configReader;

    // Defines a reusable RestAssured RequestSpecification
    protected static RequestSpecification requestSpec;

    /**
     * @BeforeClass - Runs once before any test methods in this class.
     * Initializes base URI, logging, and reusable request specification.
     */
    @BeforeClass
    public void setUp() throws FileNotFoundException {

        // Load configuration
        configReader = new ConfigReader();
        RestAssured.baseURI = Routes.base_URL;
        LogHelper.info("✅ Base URI set to: " + RestAssured.baseURI);

        // Ensure /logs directory exists and log file ready
        File logFile = new File("logs/test_log.log");
        logFile.getParentFile().mkdirs();
        PrintStream logStream = new PrintStream(new FileOutputStream(logFile, true), true);

        // Attach request and response logging filters
        RestAssured.filters(
                new RequestLoggingFilter(logStream),
                new ResponseLoggingFilter(logStream)
        );

        // Read types from config.properties for flexibility
        requestSpec = RestAssured.given()
                .contentType(configReader.getProperty("contentType"))
                .accept(configReader.getProperty("acceptType"));

        LogHelper.info("🧩 Request spec initialized with Content-Type: "
                + configReader.getProperty("contentType")
                + " | Accept: " + configReader.getProperty("acceptType"));
    }

    /**
     * @AfterSuite - Runs once after the entire test suite finishes.
     * Ensures cleanup of log streams and RestAssured sessions.
     * This prevents file-locking issues during Gradle clean.
     */
    @AfterSuite
    public void tearDown() {
        try {
            LogHelper.info("🧹 Test execution completed. Releasing all resources...");
            LogHelper.close();       // ✅ Closes the log file properly
            RestAssured.reset();     // ✅ Clears any static config and sessions
            System.gc();             // ✅ Suggests garbage collection to free file handles
            System.out.println("✅ Log resources released and environment reset.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Helper: Check if list is sorted descending
    protected boolean isSortedDescending(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) < list.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    // ✅ Helper: Check if list is sorted ascending
    protected boolean isSortedAscending(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) > list.get(i + 1)) {
                return false;
            }
        }
        return true;
    }
}
