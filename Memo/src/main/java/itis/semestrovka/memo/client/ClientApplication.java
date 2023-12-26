package itis.semestrovka.memo.client;
/**
 * Client Main Class
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {

    private static Stage stage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/itis/semestrovka/memo/choose-view.fxml"));
        primaryStage.setTitle("Client!");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setScene(Scene scene, Boolean isFullScreen) {
        stage.setScene(scene);
        if(isFullScreen){
            stage.setFullScreen(true);
        }
    }
}
