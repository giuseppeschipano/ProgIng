package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.example.clientgRPC.SceneManager;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.LoginResponse;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registratiButton;
    @FXML private Label esitoLabel;

    private final TrenicalClientImpl client = new TrenicalClientImpl("localhost", 50051);

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            esitoLabel.setText("Inserisci email e password.");
            return;
        }

        try {
            LoginResponse response = client.provaLogin(email, password);

            if (response.getSuccesso()) {
                Stage stage = (Stage) loginButton.getScene().getWindow();
                if (response.getIsAdmin()) {
                    SceneManager.switchScene(stage, "/org/example/gui/view/AdminView.fxml", "Area Admin");
                } else {
                    SceneManager.switchScene(stage, "/org/example/gui/view/HomeView.fxml", "Home Trenical");
                }
            } else {
                esitoLabel.setText("Credenziali errate. Riprova.");
            }

        } catch (Exception e) {
            esitoLabel.setText("Errore di connessione al server.");
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        registratiButton.setOnAction(e -> {
            try {
                Stage stage = (Stage) loginButton.getScene().getWindow();
                SceneManager.switchScene(stage, "/org/example/gui/view/RegistrazioneView.fxml", "Registrazione Trenical");
            } catch (Exception ex) {
                esitoLabel.setText("Errore durante il caricamento della registrazione.");
                ex.printStackTrace();
            }
        });
    }
}

