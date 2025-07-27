package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.clientgRPC.SceneManager;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.PrenotazioneResponse;

public class PrenotazioneController {

    @FXML
    private TextField cfField;
    @FXML
    private TextField idTrattaField;
    @FXML
    private TextField postoField;
    @FXML
    private TextField carrozzaField;
    @FXML
    private Button prenotaButton;
    @FXML
    private Label esitoLabel;
    @FXML
    private Hyperlink tornaHomeLink;

    private final TrenicalClientImpl client = new TrenicalClientImpl("localhost", 50051);

    @FXML
    public void initialize() {
        prenotaButton.setOnAction(e -> prenota());
        tornaHomeLink.setOnAction(e -> {
            Stage stage = (Stage) tornaHomeLink.getScene().getWindow();
            SceneManager.switchScene(stage, "/org/example/gui/view/HomeView.fxml", "Home Trenical");
        });
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
}

