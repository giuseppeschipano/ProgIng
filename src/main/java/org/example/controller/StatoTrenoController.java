package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.NotificaTrenoResponse;

import java.io.IOException;

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
        tornaHomeLink.setOnAction(event -> tornaAllaHome());
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

    private void tornaAllaHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gui/view/home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tornaHomeLink.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

