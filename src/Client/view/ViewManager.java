package Client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewManager {
    private static final Logger logger = Logger.getLogger(ViewManager.class.getName());

    // fxml paths
    public static final String MAIN_MENU = "/views/main_menu.fxml";
    public static final String LEADERBOARD = "/views/leaderboard.fxml";
    public static final String MODE = "/views/mode_menu.fxml";
    public static final String CATEGORY_MENU = "/views/category_menu.fxml";
    public static final String CLASSIC_GAME = "/views/classic_game.fxml";
    public static final String ENDLESS_GAME = "/views/endlessgame.fxml";
    public static final String SCORE_VIEW = "/views/score_view.fxml";

    private ViewManager() {
        // prevent instantiation
    }

    public static void goTo(ActionEvent event, String url, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(url));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.setResizable(false);
            stage.show();

            logger.info("Switched to " + title);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load " + title, e);
        }
    }
}

