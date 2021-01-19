package ViewController;

import DataAccess.AppointmentsData;
import DataAccess.DataDriver;
import Model.Appointments;
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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Appointment Screen Controller Class
 * To populate, listen, and handle all main appointment table interactions
 */
public class AppointmentScreenController {
    @FXML
    private TableView<Appointments> appointmentTable;

    @FXML
    private TableColumn<Appointments, Integer> appointmentIdCol;

    @FXML
    private TableColumn<Appointments, String> titleCol;

    @FXML
    private TableColumn<Appointments, String> descriptionCol;

    @FXML
    private TableColumn<Appointments, String> locationCol;

    @FXML
    private TableColumn<Appointments, String> typeCol;

    @FXML
    private TableColumn<Appointments, LocalDateTime> startCol;

    @FXML
    private TableColumn<Appointments, LocalDateTime> endCol;

    @FXML
    private TableColumn<Appointments, LocalDateTime> createDateCol;

    @FXML
    private TableColumn<Appointments, String> createdByCol;

    @FXML
    private TableColumn<Appointments, LocalDateTime> lastUpdateCol;

    @FXML
    private TableColumn<Appointments, String> lastUpdatedByCol;

    @FXML
    private TableColumn<Appointments, Integer> customerIdCol;

    @FXML
    private TableColumn<Appointments, Integer> userIdCol;

    @FXML
    private TableColumn<Appointments, Integer> contactIdCol;

    ObservableList<Appointments> appointmentsList = AppointmentsData.getAllDatabaseAppointments();
    ObservableList<Appointments> weeklyAppointmentList = FXCollections.observableArrayList();
    ObservableList<Appointments> monthlyAppointmentList = FXCollections.observableArrayList();
    LocalDate currentDate = LocalDate.from(ZonedDateTime.now());

    /**
     * Default constructor to throw SQLException for issues calling
     * getAllDatabaseAppointments() outside of method in class
     * @throws SQLException -
     */
    public AppointmentScreenController() throws SQLException {
    }

    /**
     * Reset appointment filter to show all items by setting table with database appointments again
     * @throws SQLException -
     */
    @FXML
    void filterByAll() throws SQLException {
        appointmentTable.setItems(AppointmentsData.getAllDatabaseAppointments());
    }

    /**
     * Filters using predicate with lambda implementation to filter Appointment Screen table
     * display to show only appointments within the next week.
     * ------------ LAMBDA JUSTIFICATION --------------
     * Lambda Expression used here in combination with predicate functional interface to seamlessly
     * filter appointment list by week combining lambda's easily readable code with
     * logical boolean checks to get the final weeklyAppointmentList.
     */
    @FXML
    void filterByWeek() {
        Predicate<Appointments> weeklyView = appointment -> (appointment.getStart().toLocalDate().equals(currentDate))
                || appointment.getStart().toLocalDate().isAfter(currentDate)
                && appointment.getStart().toLocalDate().isBefore((currentDate.plusWeeks(1)));
        // Use of Collection/Collectors
        List<Appointments> appointmentsByWeek = appointmentsList.stream().filter(weeklyView).collect(Collectors.toList());
        appointmentTable.setItems(weeklyAppointmentList = FXCollections.observableList(appointmentsByWeek));
    }

    /**
     * Filters using predicate with lambda implementation to filter Appointment Screen table
     * display to show only appointments within the next month.
     * ------------ LAMBDA JUSTIFICATION --------------
     * Lambda Expression used here in combination with predicate functional interface to seamlessly
     * filter appointment list by month combining lambda's easily readable code with
     * logical boolean checks to get the final monthlyAppointmentList.
     */
    @FXML
    void filterByMonth() {
        Predicate<Appointments> monthlyView = appointment -> (appointment.getStart().toLocalDate().equals(currentDate))
                || appointment.getStart().toLocalDate().isAfter((currentDate))
                && appointment.getStart().toLocalDate().isBefore((currentDate.plusMonths(1)));

        List<Appointments> appointmentsByMonth = appointmentsList.stream().filter(monthlyView).collect(Collectors.toList());
        appointmentTable.setItems(monthlyAppointmentList = FXCollections.observableList(appointmentsByMonth));
    }

    /**
     * Display Add Appointment screen for user to add a new appointment record to database
     * @param event - Clicking 'Add New Appointment' Button
     * @throws IOException -
     */
    @FXML
    void addAppointmentScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/AddAppointment.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * Display Update Appointment screen for user to change appointment data
     * @param event - Clicking 'Update Appointment' Button
     * @throws IOException -
     */
    @FXML
    void updateAppointmentScreen(ActionEvent event) throws IOException {
        try {
            Appointments modifyAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/UpdateAppointment.fxml"));
            Parent parent = loader.load();
            Scene modifyCustomerScene = new Scene(parent);
            UpdateAppointmentController controller = loader.getController();
            controller.populateAppointmentData(modifyAppointment);
            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
            window.setScene(modifyCustomerScene);
            window.show();
        }
        catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Click an Appointment to select it.");
            alert.showAndWait();
        }
    }

    /**
     * Alert user with confirmation pop-up to confirm deletion intention
     * Delete Appointment record if 'Ok" is clicked.
     * @param event - Clicking 'Ok' Button
     * @throws SQLException -
     * @throws IOException -
     */
    @FXML
    void deleteAppointment(ActionEvent event) throws SQLException, IOException {
        try {
            Appointments selectedItem = appointmentTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Delete Appointment with ID: " + selectedItem.getAppointmentId() + "?");
            alert.setContentText("Press OK to Delete or Cancel to go back.");
            Optional<ButtonType> answer = alert.showAndWait();
            if (answer.orElseThrow().equals(ButtonType.OK)) {
                AppointmentsData.deleteDatabaseAppointment(selectedItem.getAppointmentId());
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/View/AppointmentScreen.fxml"));
                loader.load();
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Click an Appointment to select it");
            alert.showAndWait();
        }
    }

    /**
     * Send user back to Main Menu
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
     * Disconnect from database, exit application.
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
     * Initializes Appointment Screen Controller
     * @throws SQLException -
     */
    @FXML
    public void initialize() throws SQLException {
        appointmentTable.setItems(AppointmentsData.getAllDatabaseAppointments());
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        createDateCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        createdByCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpdateCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
        lastUpdatedByCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        contactIdCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
    }

}
