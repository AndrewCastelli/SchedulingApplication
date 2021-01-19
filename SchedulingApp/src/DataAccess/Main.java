package DataAccess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Andrew Castelli - StudentID: 001488272
 * ------------- Software II - C195 --------------
 * DataAccess.Main class, execute to start GUI
 */
public class Main extends Application {

    public static Connection conn = null;

    public static void main(String[] args) { launch(args); }

    /**
     * Start Connection, show login screen of User Interface.
     * @param mainStage - Login Stage
     * @throws IOException -
     * @throws SQLException -
     */
    @Override
    public void start(Stage mainStage) throws IOException, SQLException {
        conn = DataDriver.startConnection();
        Parent root = FXMLLoader.load(getClass().getResource("/View/LoginScreen.fxml"));
        mainStage.setTitle("Scheduling Database Login");
        Scene startScene = new Scene(root);
        mainStage.setScene(startScene);
        mainStage.show();
    }

}
