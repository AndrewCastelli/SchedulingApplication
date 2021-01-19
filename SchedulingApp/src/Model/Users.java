package Model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Users Class
 * Represents user logging into the application
 */
public class Users {
    Integer userId;
    static String userName;
    String password;
    Date createDate;
    String createdBy;
    Timestamp lastUpdated;
    String lastUpdatedBy;

    /**
     * Empty Constructor
     * Filled primary constructor wasn't necessary for this implementation
     */
    public Users() {}

    public static String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        Users.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}
