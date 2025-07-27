package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.clientgRPC.SceneManager;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.NotificaTrenoResponse;

public class StatoTrenoController {

    @FXML
    private TextField cfField;

    @FXML
    private TextField idTrenoField;

    @FXML
    private Button cercaButton;

    @FXML
    private TextArea risultatoArea;

    @FXML
    private Hyperlink tornaHomeLink;

    private final TrenicalClientImpl client = new TrenicalClientImpl("localhost", 50051);

    @FXML
    private void initialize() {
        cercaButton.setOnAction(event -> cercaStatoTreno());
        tornaHomeLink.setOnAction(event -> {
            Stage stage = (Stage) tornaHomeLink.getScene().getWindow();
            SceneManager.switchScene(stage, "/org/example/gui/view/HomeView.fxml", "Home Trenical");
        });
    }

    private void cercaStatoTreno() {
        String cf = cfField.getText();
        String idTreno = idTrenoField.getText();
        if (cf.isEmpty() || idTreno.isEmpty()) {
            risultatoArea.setText("Inserisci tutti i campi richiesti.");
            return;
        }
        NotificaTrenoResponse response = client.statoAttualeTreno(cf, idTreno);
        risultatoArea.setText("Stato treno: " + response.getStato() +
                "Messaggio: " + response.getMessaggio() +
                "Orario stimato arrivo: " + response.getOrarioStimato());
    }
}

