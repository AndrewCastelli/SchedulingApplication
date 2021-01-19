package ViewController;

import DataAccess.AppointmentsData;
import DataAccess.DataDriver;
import Model.Appointments;
import Model.Contacts;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Update Appointment Controller Class
 */
public class UpdateAppointmentController {
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
    private TextField startField;

    @FXML
    private TextField endField;

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
    private ComboBox<Contacts> contactSelection;

    ObservableList<Contacts> contactList = FXCollections.observableArrayList();
    DateTimeFormatter fullFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    DateTimeFormatter hourFormat = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Populate appointment information fields with appropriate data from Appointment Screen
     * ----------LAMBDA JUSTIFICATION----------
     * Passed Lambda expression to foreach() function to iterate over it's elements in order to
     * find the correct contact and push contact name to selection using the fewest lines.
     * @param appointmentToUpdate - Appointment selected for update
     */
    @FXML
    public void populateAppointmentData(Appointments appointmentToUpdate)
    {
        appointmentIdField.setText(String.valueOf(appointmentToUpdate.getAppointmentId()));
        titleField.setText(appointmentToUpdate.getTitle());
        descriptionField.setText(appointmentToUpdate.getDescription());
        locationField.setText(appointmentToUpdate.getLocation());
        typeField.setText(appointmentToUpdate.getType());
        startField.setText(appointmentToUpdate.getStart().format(fullFormat));
        endField.setText(appointmentToUpdate.getEnd().format(fullFormat));
        lastUpdatedByField.setText(appointmentToUpdate.getLastUpdatedBy());
        lastUpdatedField.setText(appointmentToUpdate.getLastUpdated().format(fullFormat));
        createdByField.setText(appointmentToUpdate.getCreatedBy());
        createDateField.setText(appointmentToUpdate.getCreateDate().format(fullFormat));
        customerIdField.setText(String.valueOf(appointmentToUpdate.getCustomerId()));
        userIdField.setText(String.valueOf(appointmentToUpdate.getUserId()));
        contactIdField.setText(String.valueOf(appointmentToUpdate.getContactId()));
        contactList.forEach((contactRecord) -> {
            if (appointmentToUpdate.getContactId() == contactRecord.getContactID()) {
                String contactName = contactRecord.getContactName();
                Contacts contact = new Contacts(appointmentToUpdate.getContactId(), contactName);
                contactSelection.setValue(contact);
            }
        });
    }

    /**
     * Initialize and format ZoneId, LocalDateTime, ZonedDateTime, and LocalTime objects
     * Perform all required validation, alerting user with relevant validation alerts
     * If inputs pass validation, update database appointment record with new user inputs.
     * @param event - Clicking 'Submit' Button
     * @throws SQLException -
     * @throws IOException -
     */
    @FXML
    public void updateAppointment(ActionEvent event) throws SQLException, IOException {
        try {
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
//            ZonedDateTime zonedStartEST = zonedStart.withZoneSameInstant(estZoneId);
//            ZonedDateTime zonedEndEST = zonedEnd.withZoneSameInstant(estZoneId);
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
                        String lastUpdated = LocalDateTime.now(userZoneId).format(fullFormat);
                        AppointmentsData.updateDatabaseAppointment(Integer.valueOf(appointmentIdField.getText()),
                                titleField.getText(), descriptionField.getText(), locationField.getText(),
                                typeField.getText(), startTimeUTC, endTimeUTC, createDateField.getText(),
                                createdByField.getText(), lastUpdated, lastUpdatedByField.getText(),
                                Integer.valueOf(customerIdField.getText()), Integer.valueOf(userIdField.getText()),
                                Integer.valueOf(contactIdField.getText()));
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
     * Send user back to Appointment Screen Display
     * @param event - Clicking 'Back' Button
     * @throws IOException -
     */
    @FXML
    void back(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/AppointmentScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * Initialize Update Appointment Controller,
     * set contact box items with Contacts from Database
     * @throws SQLException -
     */
    public void initialize() throws SQLException {
        Connection conn = DataDriver.startConnection();
        ResultSet queryResult = conn.createStatement().executeQuery("SELECT * FROM contacts");
        while (queryResult.next()) {
            contactList.add(new Contacts(queryResult.getInt("Contact_ID"),
                                         queryResult.getString("Contact_Name"),
                                         queryResult.getString("Email")));
        }
        contactSelection.setItems(contactList);
        if (!contactSelection.getSelectionModel().isEmpty()) {
            Contacts contact = contactSelection.getSelectionModel().getSelectedItem();
            contactIdField.setText(String.valueOf(contact.getContactID()));
        }
    }

}

