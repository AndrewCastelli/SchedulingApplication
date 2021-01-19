package ViewController;

import DataAccess.AppointmentsData;
import DataAccess.DataDriver;
import Model.Appointments;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Month;
import java.util.Objects;
import java.util.Optional;

/**
 * Appointment Report Controller Class
 */
public class AppointmentReportController {
    @FXML
    private Label totalCountField;
    @FXML
    private Label briefingCount;
    @FXML
    private Label planningCount;
    @FXML
    private Label decemberCount;
    @FXML
    private Label novemberCount;
    @FXML
    private Label octoberCount;
    @FXML
    private Label septemberCount;
    @FXML
    private Label augustCount;
    @FXML
    private Label julyCount;
    @FXML
    private Label juneCount;
    @FXML
    private Label mayCount;
    @FXML
    private Label aprilCount;
    @FXML
    private Label marchCount;
    @FXML
    private Label februaryCount;
    @FXML
    private Label januaryCount;

    /**
     * Send user interface back to Main Menu display
     * @param event - Clicking 'Back' Button
     * @throws IOException -
     */
    @FXML
    void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/MainMenu.fxml"));
        loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Disconnect from database, Exit application
     */
    @FXML
    void exit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Exit Application?");
        alert.setContentText("Press OK to Exit or Cancel to return.");
        alert.showAndWait();
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.orElseThrow() == ButtonType.OK) {
            DataDriver.disconnect();
            System.exit(0);
        }
    }

    /**
     * Initialize Appointment Report Controller Class
     * Loop through appointments to incrementally count the total appointments,
     * appointments per month, and per type
     * @throws SQLException -
     */
    @FXML
    public void initialize() throws SQLException {
        // Loop through appointments to count each required report variable
        for (Appointments appointment : Objects.requireNonNull(AppointmentsData.getAllDatabaseAppointments())) {
            // Increment total count string for each appointment looped through
            totalCountField.setText(String.valueOf(Integer.parseInt(totalCountField.getText()) + 1));
            int planInt = Integer.parseInt(planningCount.getText());
            int briefInt = Integer.parseInt(briefingCount.getText());
            int jC = Integer.parseInt(januaryCount.getText());
            int fC = Integer.parseInt(februaryCount.getText());
            int maC = Integer.parseInt(marchCount.getText());
            int aC = Integer.parseInt(aprilCount.getText());
            int mC = Integer.parseInt(mayCount.getText());
            int junC = Integer.parseInt(juneCount.getText());
            int julC = Integer.parseInt(julyCount.getText());
            int auC = Integer.parseInt(augustCount.getText());
            int sC = Integer.parseInt(septemberCount.getText());
            int oC = Integer.parseInt(octoberCount.getText());
            int nC = Integer.parseInt(novemberCount.getText());
            int dC = Integer.parseInt(decemberCount.getText());
            Month month = appointment.getStart().getMonth();
            if (appointment.getType().toLowerCase().contains("plan")) {
                if (planInt != 0) {
                    planInt = planInt + 1;
                    planningCount.setText(String.valueOf(planInt));
                } else { planningCount.setText(String.valueOf(1)); }
            } else if (appointment.getType().toLowerCase().contains("brief")) {
                if (briefInt != 0) {
                    briefInt = briefInt + 1;
                    briefingCount.setText(String.valueOf(briefInt));
                } else { briefingCount.setText(String.valueOf(1)); }
            }  // Can add more appointment types here with more else if statements
            if (month == Month.JANUARY) {
                jC = jC + 1;
                januaryCount.setText(String.valueOf(jC));
            } else if (month == Month.FEBRUARY) {
                fC = fC + 1;
                februaryCount.setText(String.valueOf(fC));
            } else if (month == Month.MARCH) {
                maC = maC + 1;
                marchCount.setText(String.valueOf(maC));
            } else if (month == Month.APRIL) {
                aC = aC + 1;
                aprilCount.setText(String.valueOf(aC));
            } else if (month == Month.MAY) {
                mC = mC + 1;
                mayCount.setText(String.valueOf(mC));
            } else if (month == Month.JUNE) {
                junC = junC + 1;
                juneCount.setText(String.valueOf(junC));
            } else if (month == Month.JULY) {
                julC = julC + 1;
                julyCount.setText(String.valueOf(julC));
            } else if (month == Month.AUGUST) {
                auC = auC + 1;
                augustCount.setText(String.valueOf(auC));
            } else if (month == Month.SEPTEMBER) {
                sC = sC + 1;
                septemberCount.setText(String.valueOf(sC));
            } else if (month == Month.OCTOBER) {
                oC = oC + 1;
                octoberCount.setText(String.valueOf(oC));
            } else if (month == Month.NOVEMBER) {
                nC = nC + 1;
                novemberCount.setText(String.valueOf(nC));
            } else if (month == Month.DECEMBER) {
                dC = dC + 1;
                decemberCount.setText(String.valueOf(dC));
            }
        }
    }

}
