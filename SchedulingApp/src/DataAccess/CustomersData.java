package DataAccess;

import Model.Customers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;

/**
 * Customers Data Access Class
 */
public class CustomersData {
    public static ObservableList<Customers> allCustomers = FXCollections.observableArrayList();
    public static ObservableList<Customers> getAllCustomers() {
        allCustomers.clear();
        try {
            Connection conn = DataDriver.startConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM customers");
            while (rs.next()) {
                allCustomers.add(new Customers(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Postal_Code"),
                        rs.getString("Phone"),
                        rs.getTimestamp("Create_Date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        rs.getString("Last_Updated_By"),
                        rs.getInt("Division_ID")));
            }
            return allCustomers;
        }
        catch (SQLException ignored) {
        }
        return null;
    }

    /**
     * Insert Customer Data from Add Customer form into new Customer Record in database.
     * @param customerID
     * @param customerName
     * @param customerAddress
     * @param customerPostal
     * @param customerPhone
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     * @param divisionID
     * @throws SQLException -
     */
    public static void addCustomer(Integer customerID, String customerName, String customerAddress, String customerPostal, String customerPhone, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy, Integer divisionID) throws SQLException {
        Statement statement = DataDriver.startConnection().createStatement();
        statement.executeUpdate("INSERT INTO customers SET Customer_ID='"+customerID+"', Customer_Name='"+customerName+"', Address='"+customerAddress+"', Postal_Code='"+customerPostal+"', Phone='"+customerPhone+"',Create_Date='"+createDate+"', Created_By='"+createdBy+"',Last_Update='"+lastUpdate+"', Last_Updated_By='"+lastUpdatedBy+"', Division_ID= "+divisionID);
    }

    /**
     * Update Customer Data from Update Customer form into new Customer Record in database.
     * @param customerID
     * @param customerName
     * @param customerAddress
     * @param customerPostal
     * @param customerPhone
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     * @param divisionID
     * @throws SQLException
     */
    public static void updateCustomer(Integer customerID, String customerName, String customerAddress, String customerPostal, String customerPhone, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy, Integer divisionID) throws SQLException {
        Statement statement = DataDriver.startConnection().createStatement();
        statement.executeUpdate("UPDATE customers SET Customer_Name='"+customerName+"', Address='"+customerAddress+"', Postal_Code='"+customerPostal+"', Phone='"+customerPhone+"',Create_Date='"+createDate+"', Created_By='"+createdBy+"', Last_Update='"+lastUpdate+"', Last_Updated_By='"+lastUpdatedBy+"', Division_ID= "+divisionID+" WHERE Customer_ID ="+customerID);
    }

    /**
     * Delete Customer record from database where ID matches input ID
     * @param customerId - Input customer ID
     * @throws SQLException -
     */
    public static void deleteCustomer(int customerId) throws SQLException {
        Statement statement = DataDriver.startConnection().createStatement();
        String query = "DELETE FROM appointments WHERE Customer_ID=" + customerId;
        statement.executeUpdate(query);

        String queryOne = "DELETE FROM customers WHERE Customer_ID=" + customerId;
        statement.executeUpdate(queryOne);
    }

}
