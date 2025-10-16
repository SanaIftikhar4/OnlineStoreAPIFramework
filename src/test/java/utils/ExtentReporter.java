package utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReporter implements ITestListener {
    private static ExtentReports extent;
    private static ExtentSparkReporter sparkReporter;
    private static ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();
    private static Map<String, ExtentTest> testSuiteMap = new ConcurrentHashMap<>();
    private static String reportName;
    private static final Object lock = new Object();

    public synchronized static ExtentReports getExtentInstance() {
        if (extent == null) {
            synchronized (lock) {
                if (extent == null) {
                    initializeExtentReport();
                }
            }
        }
        return extent;
    }

    private static void initializeExtentReport() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        reportName = "Extent-Report-" + timeStamp + ".html";
        String reportPath = System.getProperty("user.dir") + File.separator + "reports" + File.separator + reportName;

        // Create reports directory if it doesn't exist
        new File(System.getProperty("user.dir") + File.separator + "reports").mkdirs();

        sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setDocumentTitle("API Automation Test Report");
        sparkReporter.config().setReportName("API Test Execution Summary");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setEncoding("UTF-8");

        // Additional configuration
        sparkReporter.config().setTimelineEnabled(true);
        sparkReporter.config().setCss(".badge-primary { background-color: #007bff; }");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Host Name", "Localhost");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("User", System.getProperty("user.name"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
    }

    @Override
    public void onStart(ITestContext context) {
        String suiteName = context.getSuite().getName();
        ExtentTest suiteNode = getExtentInstance().createTest(suiteName);
        testSuiteMap.put(context.getName(), suiteNode);
        suiteNode.info("Suite started: " + suiteName);
        suiteNode.assignCategory("Suite: " + suiteName);
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest suiteNode = testSuiteMap.get(result.getTestContext().getName());
        if (suiteNode != null) {
            ExtentTest methodNode = suiteNode.createNode(result.getMethod().getMethodName());
            methodNode.assignCategory(result.getTestContext().getName());

            // Add test method parameters if any
            Object[] parameters = result.getParameters();
            if (parameters != null && parameters.length > 0) {
                methodNode.info("Test Parameters: " + java.util.Arrays.toString(parameters));
            }

            testNode.set(methodNode);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = testNode.get();
        if (test != null) {
            test.log(Status.PASS, result.getMethod().getMethodName() + " passed.");
            test.info("Execution Time: " + getExecutionTime(result) + " ms");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = testNode.get();
        if (test != null) {
            test.log(Status.FAIL, result.getMethod().getMethodName() + " failed.");
            test.log(Status.FAIL, result.getThrowable());
            test.info("Execution Time: " + getExecutionTime(result) + " ms");

            // Add screenshot capability for UI tests (if applicable)
            // test.addScreenCaptureFromPath(screenshotPath);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = testNode.get();
        if (test != null) {
            test.log(Status.SKIP, result.getMethod().getMethodName() + " skipped.");
            if (result.getThrowable() != null) {
                test.info("Reason: " + result.getThrowable().getMessage());
            }
            test.info("Execution Time: " + getExecutionTime(result) + " ms");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentTest suiteNode = testSuiteMap.get(context.getName());
        if (suiteNode != null) {
            suiteNode.info("Suite finished: " + context.getSuite().getName());
            suiteNode.info("Total Tests: " + context.getAllTestMethods().length);
            suiteNode.info("Passed: " + context.getPassedTests().size());
            suiteNode.info("Failed: " + context.getFailedTests().size());
            suiteNode.info("Skipped: " + context.getSkippedTests().size());
        }

        if (extent != null) {
            extent.flush();
        }

        // Optional: Open report automatically
        if (System.getProperty("open.report", "false").equals("true")) {
            openReport();
        }
    }

    private void openReport() {
        try {
            File reportFile = new File(System.getProperty("user.dir") + File.separator + "reports" + File.separator + reportName);
            if (reportFile.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(reportFile.toURI());
            }
        } catch (IOException e) {
            System.err.println("Failed to open report: " + e.getMessage());
        }
    }

    private long getExecutionTime(ITestResult result) {
        return result.getEndMillis() - result.getStartMillis();
    }

    /**
     * Utility method to add custom logs from test methods
     */
    public static void addStepLog(String message) {
        ExtentTest test = testNode.get();
        if (test != null) {
            test.info(message);
        }
    }

    /**
     * Utility method to add API request/response details
     */
    public static void addApiDetails(String request, String response, int statusCode) {
        ExtentTest test = testNode.get();
        if (test != null) {
            test.info("<b>Request:</b> " + request);
            test.info("<b>Response Status:</b> " + statusCode);
            test.info("<b>Response:</b> " + response);
        }
    }
}