import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    //the log file name where the logs will be saved
    private static final String LOG_FILE = "ticket_system.log";

    //write the records of customer buying and vendor adding simulation to the log file with the time stamp
    public static synchronized void log(String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logMessage = timestamp + " - " + message;

        // Print to console
        System.out.println(logMessage);

        // Write to log file
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(logMessage + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
