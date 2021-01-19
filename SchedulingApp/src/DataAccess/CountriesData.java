package DataAccess;

import Model.Countries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

/**
 * Countries Data Access Class
 */
public class CountriesData {

    public static ObservableList<Countries> allCountries = FXCollections.observableArrayList();

    /**
     * Push Countries data from database to a list for use in program.
     * @return All countries from database in an observable list.
     * @throws SQLException -
     */
    public static ObservableList<Countries> getAllCountries() throws SQLException {
        allCountries.clear();
        Connection conn = DataDriver.startConnection();
        ResultSet queryResult = conn.createStatement().executeQuery("SELECT * FROM countries");
        while (queryResult.next()) {
            allCountries.add(new Countries(
                    queryResult.getInt("Country_ID"),
                    queryResult.getString("Country"),
                    queryResult.getTimestamp("Create_Date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    queryResult.getString("Created_By"),
                    queryResult.getTimestamp("Last_Update").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    queryResult.getString("Last_Updated_By")));
        }
        return allCountries;
    }

}
