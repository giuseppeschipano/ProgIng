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
            if (cf.isEmpty() || idTratta.isEmpty()) {
                esitoLabel.setText("Inserisci CF e ID Tratta.");
                return;
            }
            PrenotazioneResponse response = client.prenota(cf, idTratta); //il server assegna automaticamente posto e carrozza (invio 0)
            if (response.getSuccess()) {
                esitoLabel.setText(
                        response.getMessaggio() + "\nID Prenotazione: " + response.getIdPrenotazione() +
                                "\nPosto: " + response.getPostoPrenotazione() +
                                "\nCarrozza: " + response.getCarrozza()
                );
            } else {
                esitoLabel.setText(response.getMessaggio());
            }

        } catch (Exception ex) {
            esitoLabel.setText("Errore: " + ex.getMessage());
        }
    }
}

