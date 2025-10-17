//Enhanced BaseTest using AI tool
package base;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.*;
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
    private static final ThreadLocal<RequestSpecification> requestSpecThreadLocal = new ThreadLocal<>();
    // Loads config.properties values (base URL, contentType, etc.)
    protected static ConfigReader configReader;

    // Defines a reusable RestAssured RequestSpecification
    protected static RequestSpecification requestSpec;

    protected static final String SCHEMA_PATH = "schemas/";

    /**
     * Initializes configuration, base URI, and thread-safe request specification.
     * Runs once per test class.
     */
    /**
     * @BeforeClass - Runs once before any test methods in this class.
     * Initializes base URI, logging, and reusable request specification.
     */
    @BeforeClass(alwaysRun = true)
    public void setUp() throws FileNotFoundException {

        // Load configuration
        configReader = new ConfigReader();

        //Fetch baseURL dynamically from config.properties
        RestAssured.baseURI = configReader.getProperty("baseURL");


        LogHelper.info("✅ Base URI set to: " + RestAssured.baseURI);

        // Ensure /logs directory exists and log file ready
        File logFile = new File("logs/" + this.getClass().getSimpleName() + "_log.log");
        logFile.getParentFile().mkdirs();
        PrintStream logStream = new PrintStream(new FileOutputStream(logFile, true), true);

        RestAssured.filters(
                new RequestLoggingFilter(logStream),
                new ResponseLoggingFilter(logStream)
        );

        // Read types from config.properties for flexibility
         requestSpec = RestAssured.given()
                .contentType(configReader.getProperty("contentType"))
                .accept(configReader.getProperty("acceptType"))
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter());


        requestSpecThreadLocal.set(requestSpec);


    }
    protected RequestSpecification getRequestSpec() {
        return requestSpecThreadLocal.get();
    }

    /**
     * @AfterSuite - Runs once after the entire test suite finishes.
     * Ensures cleanup of log streams and RestAssured sessions.
     * This prevents file-locking issues during Gradle clean.
     */
    @AfterClass(alwaysRun = true)
    public void cleanUp() {
        requestSpecThreadLocal.remove();
        LogHelper.info("🧹 Cleaned up thread-local resources for " + this.getClass().getSimpleName());
    }

    @AfterSuite(alwaysRun = true)
    public void finalTearDown() {
        try {
            LogHelper.info("📦 Suite execution completed. Closing log files only...");
            LogHelper.close();
            System.out.println("✅ Log files closed. RestAssured state preserved.");
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
