package ViewController;

import DataAccess.DataDriver;
import DataAccess.ScheduleData;
import Model.Appointments;
import Model.Contacts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Contact Schedule Controller class
 */
public class ContactScheduleController {
    @FXML
    private ComboBox<Contacts> contactSelection;

    @FXML
    private TableView<Appointments> scheduleTable;

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
     * Fill schedule table with selected contact's appointment information
     * @throws SQLException -
     */
    @FXML
    void populateSchedule() throws SQLException {
            Contacts selectedContact = contactSelection.getSelectionModel().getSelectedItem();
            ScheduleData.selectContact(selectedContact);
            scheduleTable.setItems(ScheduleData.getContactSchedule());
            appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
            endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
            customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
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
     * Create list, use it to push Database Contacts to box for user to choose from
     * @throws SQLException -
     */
    @FXML
    public void initialize() throws SQLException {
        ObservableList<Contacts> contacts = FXCollections.observableArrayList();
        Connection conn = DataDriver.startConnection();
        ResultSet queryResult = conn.createStatement().executeQuery("SELECT * FROM contacts");
        while (queryResult.next()) {
            contacts.add(new Contacts(queryResult.getInt("Contact_ID"),
                                      queryResult.getString("Contact_Name"),
                                      queryResult.getString("Email")));
        }
        contactSelection.setItems(contacts);
    }

}

