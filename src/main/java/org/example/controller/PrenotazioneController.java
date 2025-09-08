package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.clientgRPC.SceneManager;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.PrenotazioneResponse;

import java.io.IOException;

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
    private Button vaiAdAcquistoButton;
    @FXML
    private Hyperlink tornaHomeLink;

    private final TrenicalClientImpl client = new TrenicalClientImpl("localhost", 50051);
    private PrenotazioneResponse lastPrenotazione; //memorizzo ultima prenotazione

    @FXML
    public void initialize() {
        prenotaButton.setOnAction(e -> prenota());
        tornaHomeLink.setOnAction(e -> {
            Stage stage = (Stage) tornaHomeLink.getScene().getWindow();
            SceneManager.switchScene(stage, "/org/example/gui/view/HomeView.fxml", "Home Trenical");
        });
        if (vaiAdAcquistoButton != null) {
            vaiAdAcquistoButton.setOnAction(e -> vaiAdAcquisto());
        }
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
            lastPrenotazione = response;
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

    private void vaiAdAcquisto() {
        if (lastPrenotazione == null || !lastPrenotazione.getSuccess()) {
            esitoLabel.setText("Prenota prima di andare all'acquisto.");
            return;
        }
        Stage stage = (Stage) vaiAdAcquistoButton.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gui/view/AcquistoBigliettoView.fxml"));
            Parent root = loader.load();
            // Recupero dati utili
            AcquistoController acqCtrl = loader.getController();
            acqCtrl.prefillFromPrenotazione(
                    cfField.getText(),
                    idTrattaField.getText(),
                    lastPrenotazione.getIdPrenotazione(),
                    lastPrenotazione.getPostoPrenotazione(),
                    lastPrenotazione.getCarrozza()
            );
            stage.setTitle("Acquisto Biglietto");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void prefillFromSearch(String idTratta) {
        if (idTrattaField != null) {
            idTrattaField.setText(idTratta);
        }
    }
}

