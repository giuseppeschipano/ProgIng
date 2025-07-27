package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.example.clientgRPC.SceneManager;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.LoginResponse;

import java.io.IOException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Hyperlink registratiLink;
    @FXML private Label esitoLabel;

    private final TrenicalClientImpl client = new TrenicalClientImpl("localhost", 50051);

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();
        if (email.isEmpty() || password.isEmpty()) {
            esitoLabel.setText("Inserisci email e password.");
            return;
        }
        LoginResponse response = client.provaLogin(email, password);
        if (response.getSuccesso()) {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            if (response.getIsAdmin()) {
                SceneManager.switchScene(stage, "/org/example/gui/view/AdminView.fxml", "Area Admin");
            } else {
                SceneManager.switchScene(stage, "/org/example/gui/view/HomeView.fxml", "Home Trenical");
            }
        }
    }

    @FXML
    private void initialize() {
        registratiLink.setOnAction(e -> {
            try {
                Stage stage = (Stage) loginButton.getScene().getWindow();
                SceneManager.switchScene(stage, "/org/example/gui/view/RegistrazioneControllerView.fxml", "Registrazione Trenical");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
