package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
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
            if (response.getIsAdmin()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gui/view/admin.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gui/view/home.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            }
        }

    }

    @FXML
    private void initialize() {
        registratiLink.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gui/view/registrazione.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Registrazione Trenical");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
