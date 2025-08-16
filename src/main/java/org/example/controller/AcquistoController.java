package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.example.clientgRPC.SceneManager;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.AcquistoResponse;
import org.example.grpc.UserDTO;


public class AcquistoController {

    @FXML
    private TextField cfField;
    @FXML
    private TextField idTrattaField;
    @FXML
    private TextField classeField;
    @FXML
    private TextField postoField;
    @FXML
    private TextField carrozzaField;
    @FXML
    private TextField numeroCartaField;
    @FXML
    private TextField prenotazioneIdField;
    @FXML
    private Label esitoLabel;
    @FXML
    private Button acquistaButton;
    @FXML
    private Hyperlink tornaHomeLink;

    private final TrenicalClientImpl client = new TrenicalClientImpl("localhost", 50051);

    @FXML
    private void initialize() {
        acquistaButton.setOnAction(this::handleAcquisto);
        tornaHomeLink.setOnAction(e -> {
            Stage stage = (Stage) tornaHomeLink.getScene().getWindow();
            SceneManager.switchScene(stage, "/org/example/gui/view/HomeView.fxml", "Home Trenical");
        });
    }

    private void handleAcquisto(ActionEvent event) {
        String cf = cfField.getText();
        String idTratta = idTrattaField.getText();
        String classe = classeField.getText();
        String numeroCarta = numeroCartaField.getText();
        String prenotazioneId = prenotazioneIdField.getText();
        if (cf.isEmpty() || idTratta.isEmpty() || classe.isEmpty()
                || numeroCarta.isEmpty() || postoField.getText().isEmpty() || carrozzaField.getText().isEmpty()) {
            esitoLabel.setText("Compila tutti i campi obbligatori.");
            return;
        }
        try {
            int posto = Integer.parseInt(postoField.getText());
            int carrozza = Integer.parseInt(carrozzaField.getText());
            UserDTO utente = UserDTO.getDefaultInstance();
            AcquistoResponse response = client.acquistaBiglietto(
                    utente, cf, idTratta, classe, posto, carrozza, numeroCarta, prenotazioneId
            );
            if (response.getSuccess()) {
                esitoLabel.setText("Acquisto riuscito! ID: " + response.getBiglietto().getIdBiglietto());
            } else {
                esitoLabel.setText("Errore: " + response.getMessaggio());
            }
        } catch (NumberFormatException e) {
            esitoLabel.setText("Posto e carrozza devono essere numeri.");
        } catch (Exception e) {
            esitoLabel.setText("Errore durante l'acquisto.");
            e.printStackTrace();
        }
    }

    public void prefillFromSearch(String idTratta, String classe) {
        if (idTrattaField != null) {
            idTrattaField.setText(idTratta);
        }
        if (classeField != null && classe != null) {
            classeField.setText(classe);
        }
    }

    public void prefillFromPrenotazione(String cf, String idTratta, String prenotazioneId, int posto, int carrozza) {
        if (cfField != null) cfField.setText(cf);
        if (idTrattaField != null) idTrattaField.setText(idTratta);
        if (prenotazioneIdField != null) prenotazioneIdField.setText(prenotazioneId);
        if (postoField != null) postoField.setText(String.valueOf(posto));
        if (carrozzaField != null) carrozzaField.setText(String.valueOf(carrozza));
    }
}
