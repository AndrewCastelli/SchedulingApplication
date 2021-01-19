package ViewController;

import DataAccess.CountriesData;
import DataAccess.CustomersData;
import DataAccess.DataDriver;
import DataAccess.DivisionsData;
import Model.Countries;
import Model.FirstLevelDivisions;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Add Customer Controller Class
 */
public class AddCustomerController {
    @FXML
    private TextField customerIdField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField postalCodeField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField createdByField;

    @FXML
    private TextField createDateField;

    @FXML
    private TextField lastUpdatedField;

    @FXML
    private TextField lastUpdatedByField;

    @FXML
    private ComboBox<FirstLevelDivisions> divisionSelection;

    @FXML
    private ComboBox<Countries> countrySelection;

    ZoneId utcZoneId = ZoneId.of("UTC");
    DateTimeFormatter fullFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Empty constructor for other classes to instantiate add customer controller
     */
    public AddCustomerController() {}

    /**
     * Grab text from each customer information field, test info for empty fields or flawed date inputs.
     * If all fields pass validation tests - create new Customer and add them to database.
     * @param event - Clicking 'Submit' Button
     * @throws IOException -
     * @throws SQLException -
     */
    @FXML
    public void addCustomer(ActionEvent event) throws IOException, SQLException {
//        int UTCoffSet = (ZonedDateTime.now().getOffset()).getTotalSeconds();
        try {
            Integer customerId = Integer.valueOf(customerIdField.getText());
            String name = nameField.getText();
            String address = addressField.getText();
            String postalCode = postalCodeField.getText();
            String phone = phoneField.getText();
            String createdUTC = LocalDateTime.now(utcZoneId).format(fullFormat);
            String updatedUTC = LocalDateTime.now(utcZoneId).format(fullFormat);
//            LocalDateTime createDate = LocalDateTime.parse(createDateField.getText(), fullFormat).minus(Duration.ofSeconds(UTCoffSet));
            String createdBy = createdByField.getText();
//            LocalDateTime lastUpdated = LocalDateTime.parse(lastUpdatedField.getText(), fullFormat).minus(Duration.ofSeconds(UTCoffSet));
            String lastUpdatedBy = lastUpdatedByField.getText();
            int divisionId = Integer.parseInt(String.valueOf(
                    divisionSelection.getSelectionModel().getSelectedItem().getDivisionId()));
            // Ensure all fields are not empty, and date inputs fit required format
            if (!nameField.getText().isBlank() && !addressField.getText().isBlank() &&
                !postalCodeField.getText().isBlank() && !phoneField.getText().isBlank() &&
                !createdByField.getText().isBlank() && !lastUpdatedByField.getText().isBlank()) {
                // Create a new customer within database, then display previous customer screen
                CustomersData.addCustomer(customerId, name, address, postalCode, phone, createdUTC,
                                          createdBy, updatedUTC, lastUpdatedBy, divisionId);
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/View/CustomerScreen.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Fields were left empty");
                alert.setContentText("All appropriate fields must be filled with customer information");
                alert.showAndWait();
            }
        } catch (DateTimeParseException | NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Text or Date/Time fields incorrectly formatted");
            alert.setContentText("Fill all fields and confirm Date/Time Formatting matches: YYYY-MM-DD HH:MM");
            alert.showAndWait();
        }
    }

    /**
     * Filter division selection list based on country, and set appropriate items for combo-box display
     * Lambda, Collections, stream, and filter used in combination to filter and display divisions.
     * ----------LAMBDA JUSTIFICATION----------
     * Used Lambda Expression for each case to filter each list in a much more readable and concise way.
     * This implementation uses fewer lines of code and leads to better code efficiency.
     * @throws SQLException
     */
    @FXML
    private void filterDivisionSelection() throws SQLException {
        ObservableList<FirstLevelDivisions> dynamicDivisionList = DivisionsData.getAllDatabaseDivisions();
        assert dynamicDivisionList != null;
        if (countrySelection.getValue() != null) {
            switch (countrySelection.getSelectionModel().getSelectedItem().getCountry()) {
                case "U.S" -> {
                    divisionSelection.setItems(DivisionsData.getAllUSDivisions());
                    {
                        List<FirstLevelDivisions> filteredUSList = dynamicDivisionList.stream().filter(f ->
                                f.getDivisionId() < 54).collect(Collectors.toList());
                        divisionSelection.setItems(FXCollections.observableList(filteredUSList));
                    }
                }
                case "Canada" -> {
                    divisionSelection.setItems(DivisionsData.getAllCADivisions());
                    {
                        List<FirstLevelDivisions> filteredCAList = dynamicDivisionList.stream().filter(f ->
                                (f.getDivisionId() > 54) && (f.getDivisionId() < 101)).collect(Collectors.toList());
                        divisionSelection.setItems(FXCollections.observableList(filteredCAList));
                    }
                }
                case "UK" -> {
                    divisionSelection.setItems(DivisionsData.getAllUKDivisions());
                    {
                        List<FirstLevelDivisions> filteredUKList = dynamicDivisionList.stream().filter(f ->
                                f.getDivisionId() >= 101).collect(Collectors.toList());
                        divisionSelection.setItems(FXCollections.observableList(filteredUKList));
                    }
                }
            }
        }
    }

    /**
     * Take user back to the previous screen without creating a new customer
     * @param event - Clicking 'Back' Button
     * @throws IOException -
     */
    @FXML
    void back(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/CustomerScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * Initialize Add Customer Controller
     * Select highest number Customer ID from database, increment ID by one for new customer.
     * Fill country box, division box, as well as created and last updated values (current date/time values)
     * @throws SQLException -
     */
    @FXML
    public void initialize() throws SQLException {
        countrySelection.setItems(CountriesData.getAllCountries());
        divisionSelection.setItems(DivisionsData.getAllDatabaseDivisions());
        createdByField.setText(String.valueOf(Users.getUserName()));
        lastUpdatedField.setText(LocalDateTime.now(ZoneId.systemDefault()).format(fullFormat));
        createDateField.setText(LocalDateTime.now(ZoneId.systemDefault()).format(fullFormat));
        lastUpdatedByField.setText(String.valueOf(Users.getUserName()));
        Connection conn = DataDriver.startConnection();
        ResultSet queryResult = conn.createStatement().executeQuery(
                "SELECT MAX(Customer_ID) AS nextCustomerID FROM customers");
        while (queryResult.next()) {
            int nextID = queryResult.getInt("nextCustomerID");
            customerIdField.setText(String.valueOf(nextID + 1));
        }
    }

}
