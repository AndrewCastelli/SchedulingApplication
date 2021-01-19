package ViewController;

import DataAccess.AppointmentsData;
import DataAccess.DataDriver;
import Model.Appointments;
import Model.Contacts;
import Model.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Add Appointment Controller Class
 */
public class AddAppointmentController {
    @FXML
    private TextField appointmentIdField;

    @FXML
    private TextField titleField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField createDateField;

    @FXML
    private TextField createdByField;

    @FXML
    private TextField lastUpdatedField;

    @FXML
    private TextField lastUpdatedByField;

    @FXML
    private TextField customerIdField;

    @FXML
    private TextField userIdField;

    @FXML
    private TextField contactIdField;

    @FXML
    private TextField startField;

    @FXML
    private TextField endField;

    @FXML
    private ComboBox<Contacts> contactSelection;

    ObservableList<Contacts> contactList = FXCollections.observableArrayList();
    DateTimeFormatter fullFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    DateTimeFormatter hourFormat = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Initialize and format ZoneId, LocalDateTime, ZonedDateTime, and LocalTime objects
     * Perform all required validation, alerting user with relevant validation alerts
     * If inputs pass validation, update database appointment record with new user inputs.
     * @param event - Clicking 'Submit' Button
     * @throws IOException -
     * @throws SQLException -
     */
    @FXML
    public void addAppointment(ActionEvent event) throws IOException, SQLException {
        try {
            Integer appointmentID = Integer.parseInt(appointmentIdField.getText());
            String title = titleField.getText();
            String description = descriptionField.getText();
            String location = locationField.getText();
            String type = typeField.getText();
            String createdBy = createdByField.getText();
            String lastUpdatedBy = lastUpdatedByField.getText();
            // Initialize and format Date/Time objects
            ZoneId userZoneId = ZoneId.systemDefault();
            ZoneId utcZoneId = ZoneId.of("UTC");
            ZoneId estZoneId = ZoneId.of("America/New_York");
            String startTimeString = startField.getText();
            String endTimeString = endField.getText();
            LocalDateTime rawStart = LocalDateTime.parse(startTimeString, fullFormat);
            LocalDateTime rawEnd = LocalDateTime.parse(endTimeString, fullFormat);
            ZonedDateTime zonedStart = rawStart.atZone(userZoneId);
            ZonedDateTime zonedEnd = rawEnd.atZone(userZoneId);
            ZonedDateTime zonedStartUTC = zonedStart.withZoneSameInstant(utcZoneId);
            ZonedDateTime zonedEndUTC = zonedEnd.withZoneSameInstant(utcZoneId);
            String startTimeUTC = zonedStartUTC.toLocalDateTime().format(fullFormat);
            String endTimeUTC = zonedEndUTC.toLocalDateTime().format(fullFormat);
            String startHourString = rawStart.atZone(estZoneId).format(hourFormat);
            String endHourString =  rawEnd.atZone(estZoneId).format(hourFormat);
            // Parse Hours & Minutes from start & end inputs for comparison with business hours
            String[] startHourArray = startHourString.split(":", 2);
            String[] endHourArray = endHourString.split(":", 2);
            int startHour = Integer.parseInt(startHourArray[0]);
            int endHour = Integer.parseInt(endHourArray[0]);
            int startMin = Integer.parseInt(startHourArray[1]);
            int endMin = Integer.parseInt(endHourArray[1]);
            LocalTime businessHoursStart = LocalTime.of(8, 0);
            LocalTime businessHoursEnd = LocalTime.of(22, 0);
            LocalTime startHours = LocalTime.of(startHour, startMin);
            LocalTime endHours = LocalTime.of(endHour, endMin);
            // All validation checks and error alerts
            boolean conflict = false;
            for (Appointments appointment : Objects.requireNonNull(AppointmentsData.allDatabaseAppointments)) {
                boolean customerConflict = Integer.parseInt(customerIdField.getText()) == appointment.getCustomerId();
                boolean sameAppointment = appointment.getAppointmentId() == Integer.parseInt(appointmentIdField.getText());
                boolean appointmentTimeConflict = rawStart.isAfter(appointment.getStart()) && rawEnd.isBefore(appointment.getEnd());
                boolean sameStart = rawStart.isEqual(appointment.getStart());
                if (!sameAppointment) {
                    if (sameStart && customerConflict || appointmentTimeConflict && customerConflict) {
                        conflict = true;
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Appointment Time Taken");
                        alert.setContentText("Customer ID: " + appointment.getCustomerId() +
                                " has an appointment already scheduled at that time");
                        alert.showAndWait();
                    }
                }
            }
            if (zonedStartUTC.isAfter(zonedEndUTC)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Appointment Start/End Times Invalid");
                alert.setContentText("Appointment start time must be before appointment end time");
                alert.showAndWait();
            } else if (startHours.isBefore(businessHoursStart) || endHours.isAfter(businessHoursEnd)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Appointment time must be during Business Hours");
                alert.setContentText("Hours of Operation: 08:00 to 22:00 Eastern Standard Time");
                alert.showAndWait();
            } else {
                // Validate no fields left blank, add new appointment if true
                if (!titleField.getText().isBlank() && !descriptionField.getText().isBlank() &&
                    !locationField.getText().isBlank() && !typeField.getText().isBlank() &&
                    !startField.getText().isBlank() && !endField.getText().isBlank() &&
                    !createDateField.getText().isBlank() && !createdByField.getText().isBlank()
                    && !lastUpdatedField.getText().isBlank() && !lastUpdatedByField.getText().isBlank()
                    && !customerIdField.getText().isBlank() && !customerIdField.getText().isBlank()
                    && !contactIdField.getText().isBlank()) {
                    if (!conflict) {
                        Integer customerId = Integer.parseInt(customerIdField.getText());
                        Integer userId = Integer.valueOf(userIdField.getText());
                        Integer contactId = Integer.valueOf(contactIdField.getText());
                        String lastUpdated = LocalDateTime.now(utcZoneId).format(fullFormat);
                        String createDate = LocalDateTime.now(utcZoneId).format(fullFormat);
                        AppointmentsData.addDatabaseAppointment(appointmentID, title, description, location, type,
                                startTimeUTC, endTimeUTC, createDate, createdBy, lastUpdated, lastUpdatedBy, customerId,
                                userId, contactId);
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/View/AppointmentScreen.fxml"));
                        loader.load();
                        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        Parent scene = loader.getRoot();
                        stage.setScene(new Scene(scene));
                        stage.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Field Empty");
                    alert.setContentText("All Fields must be filled");
                    alert.showAndWait();
                }
            }
        } catch (DateTimeParseException | NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ID Field Invalid or Date/Time Formatted Incorrectly");
            alert.setContentText("Confirm ID fields are integers and Date/Time Formatting matches: YYYY-MM-DD HH:MM");
            alert.showAndWait();
        }
    }

    /**
     * When user selects a contact, set Contact ID Field with appropriate ID
     */
    @FXML
    private void populateContacts() {
        if (!contactSelection.getSelectionModel().isEmpty()) {
            Contacts contact = contactSelection.getSelectionModel().getSelectedItem();
            contactIdField.setText(String.valueOf(contact.getContactID()));
        }
    }

    /**
     * Send user back to Appointment Screen Display
     * @param event - Clicking 'Back' Button
     * @throws IOException
     */
    @FXML
    void back(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/AppointmentScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * Initialize Add Appointment Controller
     * Set created by, last updated, and create date fields with current user and current datetime,
     * Set Appointment ID incrementation and fill contact box items with Contacts from Database.
     * @throws SQLException
     */
    @FXML
    public void initialize() throws SQLException {
        createdByField.setText(String.valueOf(Users.getUserName()));
        lastUpdatedByField.setText(String.valueOf(Users.getUserName()));
        lastUpdatedField.setText(LocalDateTime.now().format(fullFormat));
        createDateField.setText(LocalDateTime.now().format(fullFormat));
        Connection conn = DataDriver.startConnection();
        ResultSet queryResult = conn.createStatement().executeQuery(
                "SELECT MAX(Appointment_ID) AS nextAppointmentID FROM appointments");
        while (queryResult.next()) {
            int nextId = queryResult.getInt("nextAppointmentID");
            appointmentIdField.setText(String.valueOf(nextId + 1));
        }
        if (!conn.isClosed()) {
            ResultSet RS = conn.createStatement().executeQuery("SELECT * FROM contacts");
            while (RS.next()) {
                contactList.add(new Contacts(RS.getInt("Contact_ID"),
                                             RS.getString("Contact_Name"),
                                             RS.getString("Email")));
            }
        }
        contactSelection.setItems(contactList);
    }

}


