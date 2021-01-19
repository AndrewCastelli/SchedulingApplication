package DataAccess;

import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * Activity Log Writer Class
 * Using validated login boolean from UsersData Class,
 * Create (if first time) and write custom string to text file.
 */
public class ActivityLog {

    public static void log(String username, boolean validLogin) {
        try {
            FileWriter myWriter = new FileWriter("login_activity.txt", true);
            if (!validLogin) {
                myWriter.write("\nAttempted Login at " + ZonedDateTime.now() + " --  Username: " + username + " -- Outcome: Login Failed \n");
            } else {
                myWriter.write("\nAttempted Login at " + ZonedDateTime.now() + " --  Username: " + username + " -- Outcome: Successful Login \n");
            }
            myWriter.close();
        } catch (IOException ignored) { }
    }

}