package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.clientgRPC.SceneManager;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.SottoscrizioneFedeltaResponse;

public class FedeltaController {

    @FXML private TextField cfField;
    @FXML private Button sottoscriviButton;
    @FXML private Label esitoLabel;
    @FXML private Hyperlink tornaHomeLink;

    private final TrenicalClientImpl client = new TrenicalClientImpl("localhost", 50051);

    @FXML
    private void initialize() {
        sottoscriviButton.setOnAction(e -> handleSottoscrizione());
        tornaHomeLink.setOnAction(e -> {
            Stage stage = (Stage) tornaHomeLink.getScene().getWindow();
            SceneManager.switchScene(stage, "/org/example/gui/view/HomeView.fxml", "Home Trenical");
        });
    }


    private void handleSottoscrizione() {
        String cf = cfField.getText();
        if (cf.isEmpty()) {
            esitoLabel.setText("Inserisci il codice fiscale.");
            return;
        }
        SottoscrizioneFedeltaResponse response = client.sottoscriviFedelta(cf);
        if (response.getSuccess()) {
            esitoLabel.setText("Tessera creata: ID = " + response.getTessera().getId());
        } else {
            esitoLabel.setText("Errore: " + response.getMessaggio());
        }
    }
}
