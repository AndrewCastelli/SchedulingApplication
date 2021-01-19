package ViewController;

import DataAccess.CustomersData;
import DataAccess.DataDriver;
import DataAccess.ScheduleData;
import Model.Appointments;
import Model.Customers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Customer Schedule Controller Class
 */
public class CustomerScheduleController {
    @FXML
    private ComboBox<Customers> customerSelection;

    @FXML
    private TableView<Appointments> scheduledAppointmentsTable;

    @FXML
    private TableColumn<Appointments, Integer> appointmentIdCol;

    @FXML
    private TableColumn<Appointments, String> titleCol;

    @FXML
    private TableColumn<Appointments, String> typeCol;

    @FXML
    private TableColumn<Appointments, String> descriptionCol;

    @FXML
    private TableColumn<Appointments, LocalDateTime> startCol;

    @FXML
    private TableColumn<Appointments, LocalDateTime> endCol;

    @FXML
    private TableColumn<Appointments, Integer> customerIdCol;

    /**
     * Fill schedule table with selected customer's appointment info from Database
     * @throws SQLException -
     */
    @FXML
    void populateCustomerSchedule() throws SQLException {
        Customers customerSchedule = customerSelection.getSelectionModel().getSelectedItem();
        ScheduleData.selectCustomer(customerSchedule);
        scheduledAppointmentsTable.setItems(ScheduleData.getCustomerSchedule());
    }

    /**
     * Return user back to Main Menu
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
     * Close Database connection, exit application
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
     * Initialize Contact Schedule Controller
     * Push Database Customers to combo-box for to choose from, fill cells with data based on selection
     * @throws SQLException -
     */
    @FXML
    public void initialize() throws SQLException {
        customerSelection.setItems(CustomersData.getAllCustomers());
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));}

}
