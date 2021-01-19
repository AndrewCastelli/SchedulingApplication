package DataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Data Driver Class to connect to jdbc sql server
 */
public class DataDriver {
    private static Connection conn = null;
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String compositeURL = "jdbc:mysql://wgudb.ucertify.com/WJ08dcP";
    private static final String userName = "U08dcP";
    private static final String passWord = "53689258984";

    /**
     * Starts connection with SQL database
     * @return Connection object
     * @throws SQLException -
     */
    public static Connection startConnection() throws SQLException {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(compositeURL, userName, passWord);
        } catch (SQLException | ClassNotFoundException ignored) {}
        return conn;
    }

    /**
     * Disconnect from SQL server
     */
    public static void disconnect() {
        try {
            DriverManager.getConnection(compositeURL, userName, passWord).close();
        } catch (SQLException ignored) {}
    }

}



