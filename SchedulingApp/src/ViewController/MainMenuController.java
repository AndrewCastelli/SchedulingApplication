package ViewController;

import DataAccess.DataDriver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

/**
 * Main Menu Controller Class
 * Controls all FXML Logic from the Main Menu after login screen.
 */
public class MainMenuController {

    /**
     * Display Appointment Screen
     * @param event - Clicking 'Appointments' Button
     * @throws IOException -
     */
    @FXML
    void displayAppointments(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/AppointmentScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * Display Customers Screen
     * @param event - Clicking 'Customers' Button
     * @throws IOException -
     */
    @FXML
    void displayCustomers(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/CustomerScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * Display Customer Schedules Screen
     * @param event - Clicking 'Customer Schedules' Button
     * @throws IOException -
     */
    @FXML
    void displayCustomerSchedules(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/CustomerSchedule.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * Display Contact Schedules Screen
     * @param event - Clicking 'Contact Schedules' Button
     * @throws IOException -
     */
    @FXML
    void displayContactSchedules(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/ContactSchedule.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * Display Appointments Report Screen
     * @param event - Clicking 'Appointment Report' Button
     * @throws IOException -
     */
    @FXML
    void displayReports(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/AppointmentReportScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * Go back to login screen
     * @param event - Clicking 'Back' Button
     * @throws IOException -
     */
    @FXML
    void displayLogin(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/LoginScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * Disconnect from database, exit application.
     */
    @FXML
    void exit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Exit Application?");
        alert.setContentText("Press OK to Exit or Cancel to return.");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.orElseThrow() == ButtonType.OK) {
            DataDriver.disconnect();
            System.exit(0);
        }
    }

    /**
     * Initializes Main Menu Controller Class
     */
    public void initialize() {}

}





