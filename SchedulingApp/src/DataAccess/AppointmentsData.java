package DataAccess;

import Model.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * Appointments Data Access Class
 */
public class AppointmentsData {

    public static ObservableList<Appointments> allDatabaseAppointments = FXCollections.observableArrayList();

    /**
     * Push appointment records from database to an observable list for fxml access
     * @return All appointments from database in an observable list.
     * @throws SQLException -
     */
    public static ObservableList<Appointments> getAllDatabaseAppointments() throws SQLException {
        // Remove previously added elements from list to avoid duplication over multiple calls
        allDatabaseAppointments.clear();
        Connection conn = DataDriver.startConnection();
        ResultSet queryResult = conn.createStatement().executeQuery("SELECT * FROM appointments");
        while (queryResult.next()) {
            allDatabaseAppointments.add(new Appointments(
                queryResult.getInt("Appointment_ID"),
                queryResult.getString("Title"),
                queryResult.getString("Description"),
                queryResult.getString("Location"),
                queryResult.getString("Type"),
                queryResult.getTimestamp("Start").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                queryResult.getTimestamp("End").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                queryResult.getTimestamp("Create_Date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                queryResult.getString("Created_By"),
                queryResult.getTimestamp("Last_Update").toInstant().atZone(ZoneOffset.systemDefault()).toLocalDateTime(),
                queryResult.getString("Last_Updated_By"),
                queryResult.getInt("Customer_ID"),
                queryResult.getInt("User_ID"),
                queryResult.getInt("Contact_ID")));
        }
        return allDatabaseAppointments;
    }

    /**
     * Insert a new Appointment record into appointments database table
     * @param appointmentID
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param createDate
     * @param createdBy
     * @param lastUpdated
     * @param lastUpdatedBy
     * @param customerID
     * @param userID
     * @param contactID
     * @throws SQLException
     */
    public static void addDatabaseAppointment(Integer appointmentID, String title, String description,
                                              String location, String type, String start, String end,
                                              String createDate, String createdBy, String lastUpdated,
                                              String lastUpdatedBy, Integer customerID, Integer userID,
                                              Integer contactID) throws SQLException {
        Statement statement = DataDriver.startConnection().createStatement();
        statement.executeUpdate("INSERT INTO appointments SET Appointment_ID='"+appointmentID+"', Title='"+title+"'," + "Description='"+description+"', Location='"+location+"', Type='"+type+"', Start='"+start+"', " + "End='"+end+"', Create_Date='"+createDate+"',  Created_By='"+createdBy+"', Last_Update='"+lastUpdated+"', " + "Last_Updated_By='"+lastUpdatedBy+"', Customer_ID='"+customerID+"', " + "User_ID='"+userID+"', " + "Contact_ID="+contactID);
    }

    /**
     * Update appointment record in database with new inputs from user interface
     * @param appointmentID
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     * @param customerID
     * @param userID
     * @param contactID
     * @throws SQLException
     */
    public static void updateDatabaseAppointment(Integer appointmentID, String title, String description,
                                                 String location, String type, String start, String end,
                                                 String createDate, String createdBy, String lastUpdate,
                                                 String lastUpdatedBy, Integer customerID, Integer userID,
                                                 Integer contactID) throws SQLException {
        Statement statement = DataDriver.startConnection().createStatement();
        statement.executeUpdate("UPDATE appointments SET Title='"+title+"',Description='"+description+"', Location='"+location+"', Type='"+type+"', Start='"+start+"', End='"+end+"', Create_Date='"+createDate+"',  Created_By='"+createdBy+"', Last_Update='"+lastUpdate+"', Last_Updated_By='"+lastUpdatedBy+"', Customer_ID='"+customerID+"', User_ID='"+userID+"', Contact_ID="+contactID+" WHERE Appointment_ID ="+appointmentID);
    }

    /**
     * Remove a database appointment record matching the appointment ID
     * @param appointmentId - ID to match with database record selected for removal
     * @throws SQLException -
     */
    public static void deleteDatabaseAppointment(int appointmentId) throws SQLException {
        Statement statement = DataDriver.startConnection().createStatement();
        String query = "DELETE FROM appointments WHERE Appointment_ID=" + appointmentId;
        statement.executeUpdate(query);
    }

}
