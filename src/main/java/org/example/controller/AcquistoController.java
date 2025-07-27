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

    @FXML private TextField cfField;
    @FXML private TextField idTrattaField;
    @FXML private TextField classeField;
    @FXML private TextField postoField;
    @FXML private TextField carrozzaField;
    @FXML private TextField numeroCartaField;
    @FXML private TextField prenotazioneIdField;
    @FXML private Label esitoLabel;
    @FXML private Button acquistaButton;
    @FXML private Hyperlink tornaHomeLink;

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
}
