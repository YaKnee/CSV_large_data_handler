package orders;

import javafx.application.Application;
// import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import orders.GUI.*;

//TO DO
////Add Threading for Buttons
////Selector for States/Customers + Search bars         (done!)
////View summary of orders of customer                  (done!)
////Customers per State                                 (done!)
////Average Sales amounts of the orders                 (done?)
////Customer with most sales                            (done!)
////Count segments                                      (done!)
////total sales per year                                (done!)
////total sales per region                              (done!)
////****Testing****
////JavaDocs
////Make Pretty


public class App extends Application{

    /**
     * The main method of the application. It launches the JavaFX application.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This method is called when the application is started. It sets up the
     * primary stage and initializes the main scene.
     * 
     * @param stage the primary stage for this application, onto which the
     * application scene can be set
     * @throws Exception if an error occurs during the initialization
     */
    @Override
    public void start(Stage stage) throws Exception {
        try {
            //  Parent root = FXMLLoader.load(getClass().getResource("/welcome.fxml"));
            // scene.getStylesheets().add("stylesheet.css");
            Scenes scenes = new Scenes(stage);
            Scene welcomeScene = scenes.createWelcomeScene();
            Image icon = new Image("icon.png");
            stage.getIcons().add(icon);
            stage.setScene(welcomeScene);
            stage.setTitle("SuperStore Data");
            stage.setOnCloseRequest(event -> System.exit(0));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
