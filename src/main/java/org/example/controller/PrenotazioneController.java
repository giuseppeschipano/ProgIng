package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.PrenotazioneResponse;

import java.io.IOException;

public class PrenotazioneController {

    @FXML private TextField cfField;
    @FXML private TextField idTrattaField;
    @FXML private TextField postoField;
    @FXML private TextField carrozzaField;
    @FXML private Button prenotaButton;
    @FXML private Label esitoLabel;
    @FXML private Hyperlink tornaHomeLink;

    private final TrenicalClientImpl client = new TrenicalClientImpl("localhost", 50051);

    @FXML
    public void initialize() {
        prenotaButton.setOnAction(e -> prenota());
        tornaHomeLink.setOnAction(e -> tornaAllaHome());
    }

    private void prenota() {
        try {
            String cf = cfField.getText();
            String idTratta = idTrattaField.getText();
            int posto = Integer.parseInt(postoField.getText());
            int carrozza = Integer.parseInt(carrozzaField.getText());

            PrenotazioneResponse response = client.prenota(cf, idTratta, posto, carrozza);
            esitoLabel.setText(response.getMessaggio());
        } catch (Exception ex) {
            esitoLabel.setText("Errore: " + ex.getMessage());
        }
    }

    private void tornaAllaHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gui/view/login.fxml")); // o home.fxml
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) tornaHomeLink.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Trenical - Home");

        } catch (IOException e) {
            esitoLabel.setText("Errore nel caricamento della Home.");
            e.printStackTrace();
        }
    }
}

