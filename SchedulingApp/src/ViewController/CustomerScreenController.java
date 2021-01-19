package ViewController;

import DataAccess.CustomersData;
import DataAccess.DataDriver;
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
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Customer Screen Controller Class
 */
public class CustomerScreenController {
    @FXML
    private TableView<Customers> customersTable;

    @FXML
    private TableColumn<Customers, Integer> customerIdCol;

    @FXML
    private TableColumn<Customers, String> nameCol;

    @FXML
    private TableColumn<Customers, String> addressCol;

    @FXML
    private TableColumn<Customers, String> postalCodeCol;

    @FXML
    private TableColumn<Customers, String> phoneCol;

    @FXML
    private TableColumn<Customers, LocalDateTime> createDateCol;

    @FXML
    private TableColumn<Customers, String> createdByCol;

    @FXML
    private TableColumn<Customers, Timestamp> lastUpdatedCol;

    @FXML
    private TableColumn<Customers, String> lastUpdatedByCol;

    @FXML
    private TableColumn<Customers, Integer> divisionIdCol;

    /**
     * Main constructor to initialize the controller
     * Execute update query to set customers table to auto-increment
     */
    public CustomerScreenController() throws SQLException {
        try {
            Statement statement = DataDriver.startConnection().createStatement();
            statement.executeUpdate("ALTER TABLE customers AUTO_INCREMENT");
        } catch (SQLException ignored) {}
    }

    /**
     * Display Add Customer UI Form
     * @param event - Clicking 'Add Customer' Button
     * @throws IOException -
     */
    @FXML
    void addCustomer(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/AddCustomer.fxml"));
        loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Display update customer screen, alert user if no record selection was made.
     * @param event - Clicking 'Update Customer' Button
     * @throws IOException -
     * @throws SQLException -
     */
    @FXML
    void updateCustomer(ActionEvent event) throws IOException, SQLException {
        try {
            Customers selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/UpdateCustomer.fxml"));
            Parent parent = loader.load();
            Scene modifyCustomerScene = new Scene(parent);
            UpdateCustomerController controller = loader.getController();
            controller.populateCustomerData(selectedCustomer);
            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
            window.setScene(modifyCustomerScene);
            window.show();
        }
        catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("Click the customer record you wish to edit");
            alert.showAndWait();
        }
    }

    /**
     * Prompt User with warning to confirm deletion
     * If confirmed - Delete Customer record from table and database
     * @param event - Clicking 'Delete Customer' Button
     * @throws IOException -
     * @throws SQLException -
     */
    @FXML
    void deleteCustomer(ActionEvent event) throws IOException, SQLException {
        try {
            Customers selectedItem = customersTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Confirm Deletion");
            alert.setContentText("Delete Customer: " + selectedItem.getCustomerName() + " ?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                CustomersData.deleteCustomer(selectedItem.getCustomerId());
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/View/CustomerScreen.fxml"));
                loader.load();
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("Click on a Customer Record to select for deletion");
            alert.showAndWait();
        }
    }

    /**
     * Go back to previous Main Menu screen.
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
     * Close connection to database, exit application.
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
     * Initialize Customer Main Screen Controller
     * Populates customer record table values from database entry column names
     * @throws SQLException -
     */
    @FXML
    public void initialize() throws SQLException {
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postal"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        createDateCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        createdByCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpdatedCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
        lastUpdatedByCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        divisionIdCol.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
        customersTable.setItems(CustomersData.getAllCustomers());
    }

}
