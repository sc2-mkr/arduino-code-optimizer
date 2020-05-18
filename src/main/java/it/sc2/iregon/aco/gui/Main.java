package it.sc2.iregon.aco.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/main.fxml"));
        Parent root = loader.load();
        stage.setTitle("ACO - Arduino Code Optimizer");

        Scene scene = new Scene(root);
        scene.getStylesheets().add("style.css");

        stage.setScene(scene);
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/favicon.png")));

        // Pass stage to controller
        Controller controller = loader.getController();
        controller.setStage(stage);

        stage.show();
    }
}
