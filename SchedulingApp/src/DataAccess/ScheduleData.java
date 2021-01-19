package DataAccess;

import Model.Appointments;
import Model.Contacts;
import Model.Customers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

/**
 * Schedule Data Access Class
 */
public class ScheduleData {
    // Instantiate Schedule Lists to filter based on user selection
    private static Contacts newContactSchedule;
    private static Customers newCustomerSchedule;
    public static void selectContact(Contacts contactSchedule) { newContactSchedule = contactSchedule; }
    public static void selectCustomer(Customers customerSchedule) { newCustomerSchedule = customerSchedule; }
    public static ObservableList<Appointments> contactSchedule = FXCollections.observableArrayList();
    public static ObservableList<Appointments> customerSchedule = FXCollections.observableArrayList();

    /**
     * Get List of appointments from database where contact ID matches input ID.
     * @return Schedule of contact's appointments listed.
     * @throws SQLException -
     * @throws NullPointerException -
     */
    public static ObservableList<Appointments> getContactSchedule() throws SQLException, NullPointerException {
        contactSchedule.clear();
        Connection conn = DataDriver.startConnection();
        ResultSet queryResult = conn.createStatement().executeQuery("SELECT * FROM appointments WHERE Contact_ID="
                                                                    + newContactSchedule.getContactID());
        while (queryResult.next()) {
            contactSchedule.add(new Appointments(
                    queryResult.getInt("Appointment_ID"),
                    queryResult.getString("Title"),
                    queryResult.getString("Description"),
                    queryResult.getString("Type"),
                    queryResult.getTimestamp("Start").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    queryResult.getTimestamp("End").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    queryResult.getInt("Customer_ID")));
        }
        return contactSchedule;
    }

    /**
     * Get List of appointments from database where customer ID matches input ID.
     * @return Schedule of customer's appointments listed.
     * @throws NullPointerException -
     * @throws SQLException -
     */
    public static ObservableList<Appointments> getCustomerSchedule() throws NullPointerException, SQLException {
        customerSchedule.clear();
        Connection conn = DataDriver.startConnection();
        ResultSet queryResult = conn.createStatement().executeQuery("SELECT * FROM appointments WHERE Customer_ID="
                                                                    + newCustomerSchedule.getCustomerId());
        while (queryResult.next()) {
            customerSchedule.add(new Model.Appointments(
                    queryResult.getInt("Appointment_ID"),
                    queryResult.getString("Title"),
                    queryResult.getString("Description"),
                    queryResult.getString("Type"),
                    queryResult.getTimestamp("Start").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    queryResult.getTimestamp("End").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    queryResult.getInt("Customer_ID")));
        }
        return customerSchedule;
    }

}

