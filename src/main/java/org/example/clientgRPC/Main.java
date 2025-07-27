package org.example.clientgRPC;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager.switchScene(primaryStage, "/org/example/gui/view/LoginView.fxml", "Login Trenical");
    }
    public static void main(String[] args) {
        launch(args);
    }
}
