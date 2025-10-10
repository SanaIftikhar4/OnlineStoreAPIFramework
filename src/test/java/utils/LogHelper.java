package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogHelper {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String LOG_FILE_PATH = "logs/test_log.log";
    private static PrintWriter writer;
    private static boolean isClosed = false;


    static {
        try {
            File logFile = new File(LOG_FILE_PATH);
            logFile.getParentFile().mkdirs(); // ensure /logs folder exists
            writer = new PrintWriter(new FileWriter(logFile, true), true); // append mode
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to initialize log file: " + e.getMessage());
        }
    }

    private static String formatMessage(String level, String message) {
        String timestamp = dateFormat.format(new Date());
        return "[" + timestamp + "] [" + level + "] " + message;
    }
    /** Logs INFO-level messages */
    public static synchronized void info(String message) {
        log("INFO", message);
    }

    /** Logs WARN-level messages */
    public static synchronized void warn(String message) {
        log("WARN", message);
    }

    /** Logs ERROR-level messages and stack traces */
    public static synchronized void error(String message, Throwable t) {
        log("ERROR", message);
        if (t != null && writer != null) {
            t.printStackTrace(writer);
            t.printStackTrace(System.err);
        }
    }

    /** Core method that writes formatted log messages to file and console */
    private static synchronized void log(String level, String message) {
        if (isClosed) return; // skip logging if closed
        String formatted = formatMessage(level, message);
        System.out.println(formatted);
        if (writer != null) {
            writer.println(formatted);
            writer.flush();
        }
    }

    /** Safely closes the log writer — fixes Windows file lock issue */
    public static synchronized void close() {
        if (!isClosed && writer != null) {
            try {
                info("🔒 Closing log file...");
                writer.flush();
                writer.close();
                writer = null;
                isClosed = true;
                System.out.println("[INFO] Log file closed successfully.");
            } catch (Exception e) {
                System.err.println("[ERROR] Failed to close log file: " + e.getMessage());
            }
        }
    }
}