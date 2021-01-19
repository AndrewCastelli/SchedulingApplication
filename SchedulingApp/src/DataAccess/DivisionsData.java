package DataAccess;


import Model.FirstLevelDivisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

/**
 * Divisions Data Access Class
 */
public class DivisionsData {
    public static ObservableList<FirstLevelDivisions> allDatabaseDivisions = FXCollections.observableArrayList();

    /**
     * Get all First Level Divisions records from database, and return listed divisions.
     * @return First Level Divisions observable list.
     * @throws SQLException -
     */
    public static ObservableList<FirstLevelDivisions> getAllDatabaseDivisions() throws SQLException {
        allDatabaseDivisions.clear();
        Connection conn = DataDriver.startConnection();
        ResultSet queryResult = conn.createStatement().executeQuery("SELECT * FROM first_level_divisions");
        while (queryResult.next()) {
            allDatabaseDivisions.add(new FirstLevelDivisions(
                    queryResult.getInt("Division_ID"),
                    queryResult.getString("Division"),
                    queryResult.getTimestamp("Create_Date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    queryResult.getString("Created_By"),
                    queryResult.getTimestamp("Last_Update").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    queryResult.getString("Last_Updated_By"),
                    queryResult.getInt("Country_ID")));
        }
        return allDatabaseDivisions;
    }
    public static ObservableList<FirstLevelDivisions> allUSDivisions = FXCollections.observableArrayList();

    /**
     * Get all US Divisions from database and add them to a list.
     * @return List of all US divisions
     * @throws SQLException -
     */
    public static ObservableList<FirstLevelDivisions> getAllUSDivisions() throws SQLException {
        allUSDivisions.clear();
        Connection conn = DataDriver.startConnection();
        ResultSet queryResult = conn.createStatement().executeQuery("SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 1");
        while (queryResult.next()) {
            allUSDivisions.add(new FirstLevelDivisions(
                    queryResult.getInt("Division_ID"),
                    queryResult.getString("Division"),
                    queryResult.getTimestamp("Create_Date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    queryResult.getString("Created_By"),
                    queryResult.getTimestamp("Last_Update").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    queryResult.getString("Last_Updated_By"),
                    queryResult.getInt("Country_ID")));
        }
        return allUSDivisions;
    }

    public static ObservableList<FirstLevelDivisions> allUKDivisions = FXCollections.observableArrayList();

    /**
     * Get all UK Divisions from database and add them to a list.
     * @return List of all UK divisions.
     * @throws SQLException -
     */
    public static ObservableList<FirstLevelDivisions> getAllUKDivisions() throws SQLException {
        allUKDivisions.clear();
        Connection conn = DataDriver.startConnection();
        ResultSet queryResult = conn.createStatement().executeQuery("SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 2");
        while (queryResult.next()) {
            allUKDivisions.add(new FirstLevelDivisions(
                    queryResult.getInt("Division_ID"),
                    queryResult.getString("Division"),
                    queryResult.getTimestamp("Create_Date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    queryResult.getString("Created_By"),
                    queryResult.getTimestamp("Last_Update").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    queryResult.getString("Last_Updated_By"),
                    queryResult.getInt("Country_ID")));
        }
        return allUKDivisions;
    }

    public static ObservableList<FirstLevelDivisions> allCADivisions = FXCollections.observableArrayList();

    /**
     * Get all Canada Divisions from database and add them to a list
     * @return List of all Canada Divisions
     * @throws SQLException -
     */
    public static ObservableList<FirstLevelDivisions> getAllCADivisions() throws SQLException {
        allCADivisions.clear();
        Connection conn = DataDriver.startConnection();
        ResultSet queryResult = conn.createStatement().executeQuery("SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 3");
        while (queryResult.next()) {
            allCADivisions.add(new FirstLevelDivisions(
                    queryResult.getInt("Division_ID"),
                    queryResult.getString("Division"),
                    queryResult.getTimestamp("Create_Date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    queryResult.getString("Created_By"),
                    queryResult.getTimestamp("Last_Update").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    queryResult.getString("Last_Updated_By"),
                    queryResult.getInt("Country_ID")));
        }
        return allCADivisions;
    }

}
