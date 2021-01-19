package DataAccess;

import Model.Users;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static DataAccess.ActivityLog.log;

/**
 * User Data Access Class
 */
public class UsersData {

    /**
     * Instantiate new User for login, query database with login inputs for User Name and Password.
     * @param username - input User Name
     * @param password - input Password
     * @return True if User Name and Password exists in database, False otherwise
     * @throws SQLException -
     */
    public static boolean login(String username, String password) throws SQLException {
        Users currentUser = new Users();
        String loginQuery = "SELECT * FROM users WHERE User_Name='" + username + "'AND Password='" + password + "'";
        Connection conn = DataDriver.startConnection();
        ResultSet queryResult = conn.createStatement().executeQuery(loginQuery);
        if (queryResult.next()) {
            currentUser.setUserName(queryResult.getString("User_Name"));
            currentUser.setPassword(queryResult.getString("Password"));
            log(username, true);
            return true;
        } else {
            log(username, false);
            return false;
        }
    }

}
