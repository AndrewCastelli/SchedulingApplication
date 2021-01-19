package ViewController;

import DataAccess.AppointmentsData;
import DataAccess.DataDriver;
import DataAccess.UsersData;
import Model.Appointments;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Login Screen Controller Class
 */
public class LoginScreenController {
    @FXML
    private Label loginLabel;
    @FXML
    private Label userLocationLabel;
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button exitButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label locationLabel;
    private String genericAlert;
    private String errorMessage;

    /**
     * Query database to determine if login credentials are valid, if they are
     * loop through database appointments to determine if an appointment is taking place within 15 minutes
     * Alert user accordingly, proceed to Main Menu
     * @param event - Clicking 'Submit' Button
     * @throws IOException -
     * @throws SQLException -
     */
    @FXML
    void userLogin(ActionEvent event) throws IOException, SQLException {
        String userNameInput = userNameField.getText();
        String passwordInput = passwordField.getText();
        boolean loginSuccess = UsersData.login(userNameInput, passwordInput);
        boolean upcoming = false;
        if (loginSuccess) {
            AppointmentsData.getAllDatabaseAppointments();
            for (Appointments appointment : AppointmentsData.allDatabaseAppointments) {
                LocalDateTime currentTime = LocalDateTime.now();
                // If appointment hasn't started yet, and 15 minutes from now, the appointment has started, alert user
                if (currentTime.isBefore(appointment.getStart()) && currentTime.plusMinutes(15).isAfter(appointment.getStart())){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Appointment Starting Soon");
                    alert.setContentText("Appointment ID: " + appointment.getAppointmentId() + " begins at " + appointment.getStart());
                    alert.showAndWait();
                    upcoming = true;
                    break;
                }
            } if (!upcoming) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No Appointments Starting Soon");
                alert.setContentText("You have no upcoming appointments in the next 15 minutes");
                alert.showAndWait();
            }
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(genericAlert);
            alert.setContentText(errorMessage);
            alert.showAndWait();
        }
    }

    /**
     * Disconnect from database, exit application
     */
    @FXML
    void exit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Exit Application?");
        alert.setContentText("Press OK to Exit or Cancel to return.");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.orElseThrow().equals(ButtonType.OK)) {
            DataDriver.disconnect();
            System.exit(0);
        }
    }

    /**
     * Initialize Login Screen Controller, set login prompts and country based on locale
     */
    public void initialize() {
        ResourceBundle loginLanguage = ResourceBundle.getBundle("Resource/Lang", Locale.getDefault());
        locationLabel.setText(Locale.getDefault().getCountry());
        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
            loginLabel.setText(loginLanguage.getString("userLoginLabel"));
            userLocationLabel.setText(loginLanguage.getString("userLocationLabel"));
            userNameField.setPromptText(loginLanguage.getString("userIDField"));
            passwordField.setPromptText(loginLanguage.getString("passwordField"));
            loginButton.setText(loginLanguage.getString("loginButton"));
            exitButton.setText(loginLanguage.getString("exitButton"));
            genericAlert = loginLanguage.getString("genericAlert");
            errorMessage = loginLanguage.getString("errorMessage");
        }
    }

}