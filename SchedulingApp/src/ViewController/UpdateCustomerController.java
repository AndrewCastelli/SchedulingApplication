package ViewController;

import DataAccess.CountriesData;
import DataAccess.CustomersData;
import DataAccess.DivisionsData;
import Model.Countries;
import Model.Customers;
import Model.FirstLevelDivisions;
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
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Update Customer Controller Class
 */
public class UpdateCustomerController {

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
    private TextField lastUpdatedByField;

    @FXML
    private TextField lastUpdatedField;

    @FXML
    private TextField createdByField;

    @FXML
    private TextField createDateField;

    @FXML
    private ComboBox<Countries> countrySelection;

    @FXML
    private ComboBox<FirstLevelDivisions> divisionSelection;

    DateTimeFormatter fullFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Empty constructor for other classes to instantiate update customer controller
     */
    public UpdateCustomerController() {}

    /**
     * Populate customer data using main customer table inputs.
     */
    @FXML
    public void populateCustomerData(Customers selectedCustomer) throws SQLException {
        customerIdField.setText(String.valueOf(selectedCustomer.getCustomerId()));
        nameField.setText(selectedCustomer.getCustomerName());
        addressField.setText(selectedCustomer.getAddress());
        postalCodeField.setText(selectedCustomer.getPostal());
        phoneField.setText(String.valueOf(selectedCustomer.getPhone()));
        lastUpdatedByField.setText(selectedCustomer.getLastUpdatedBy());
        lastUpdatedField.setText(selectedCustomer.getLastUpdated().format(fullFormat));
        createdByField.setText(selectedCustomer.getCreatedBy());
        createDateField.setText(selectedCustomer.getCreateDate().format(fullFormat));
        updateDivision(selectedCustomer);
    }

    /**
     * Custom listener for division combo box to update division box based on ID number
     * when a country selection is made.
     */
    @FXML void updateDivision(Customers selectedCustomer) throws SQLException {
        int newDivisionId = selectedCustomer.getDivisionId();
        FirstLevelDivisions division = new FirstLevelDivisions(newDivisionId);
        if (division.getDivisionId() <= 54) {
            String US = "U.S";
            Countries country = new Countries(US);
            countrySelection.setValue(country);
            divisionSelection.setValue(division);
            updateDivisionSelection();
        } else if (division.getDivisionId() > 54 && division.getDivisionId() <= 72) {
            String UK = "UK";
            Countries country = new Countries(UK);
            countrySelection.setValue(country);
            divisionSelection.setValue(division);
            updateDivisionSelection();
        } else if (division.getDivisionId() > 72) {
            String CA = "Canada";
            Countries country = new Countries(CA);
            countrySelection.setValue(country);
            divisionSelection.setValue(division);
            updateDivisionSelection();
        }
    }

    /**
     * Filter division selection list based on country, and set appropriate items for combo-box display
     * Lambda, Collections, stream, and filter used in combination to filter and display divisions.
     * ----------LAMBDA JUSTIFICATION----------
     * Used Lambda Expression for each case to filter each list in a much more readable and concise way.
     * This implementation uses fewer lines of code and leads to better code efficiency.
     * @throws SQLException -
     */
    @FXML
    private void updateDivisionSelection() throws SQLException {
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
     * Parse and populate customer data fields for customer selected from table.
     * @param event - Clicking 'Submit' Button
     * @throws SQLException -
     * @throws IOException -
     */
    @FXML
    public void updateCustomer(ActionEvent event) throws SQLException, IOException {
        try {
            CustomersData.updateCustomer(Integer.valueOf(customerIdField.getText()),
                    nameField.getText(),
                    addressField.getText(),
                    postalCodeField.getText(),
                    phoneField.getText(),
                    createDateField.getText(),
                    createdByField.getText(),
                    lastUpdatedField.getText(),
                    lastUpdatedByField.getText(),
                    Integer.valueOf(String.valueOf(divisionSelection.getValue().getDivisionId())));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/CustomerScreen.fxml"));
            loader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (DateTimeParseException | NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Text or Date/Time fields incorrectly formatted");
            alert.setContentText("Fill all fields and confirm Date/Time Formatting matches: YYYY-MM-DD HH:MM");
            alert.showAndWait();
        }
    }

    /**
     * Go back to central Customer Screen
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
     * Initializes Update Customer Controller
     * @throws SQLException -
     */
    @FXML
    public void initialize() throws SQLException {
        countrySelection.setItems(CountriesData.getAllCountries());
        divisionSelection.setItems(DivisionsData.getAllDatabaseDivisions());
    }

}
