package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.RegistrazioneResponse;

public class RegistrazioneController {

    @FXML private TextField nomeField;
    @FXML private TextField cognomeField;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private TextField cfField;
    @FXML private TextField indirizzoField;
    @FXML private TextField dataNascitaField;
    @FXML private Label esitoLabel;
    @FXML private Hyperlink loginLink;
    @FXML private Button registratiButton;

    private final TrenicalClientImpl client = new TrenicalClientImpl("localhost", 50051);

    @FXML
    public void initialize() {
        loginLink.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gui/view/login.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Login Trenical");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        registratiButton.setOnAction(this::registraUtente);
    }

    private void registraUtente(ActionEvent event) {
        RegistrazioneResponse response = client.registrazione(
                nomeField.getText(),
                cognomeField.getText(),
                emailField.getText(),
                passwordField.getText(),
                cfField.getText(),
                indirizzoField.getText(),
                dataNascitaField.getText()
        );

        esitoLabel.setText(response.getMessaggio());
    }
}

