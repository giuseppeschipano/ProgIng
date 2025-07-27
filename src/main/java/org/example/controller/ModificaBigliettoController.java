package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.ModificaBigliettoResponse;
import org.example.clientgRPC.SceneManager;

public class ModificaBigliettoController {

    @FXML private TextField idBigliettoField;
    @FXML private TextField nuovaDataField;
    @FXML private TextField nuovaOraField;
    @FXML private TextField nuovaClasseField;
    @FXML private Label risultatoLabel;
    @FXML private Button simulaButton;
    @FXML private Button confermaButton;
    @FXML private Hyperlink tronaHomeLink;

    private final TrenicalClientImpl client = new TrenicalClientImpl("localhost", 50051);

    @FXML
    public void initialize() {
        simulaButton.setOnAction(e -> modificaBiglietto(false));
        confermaButton.setOnAction(e -> modificaBiglietto(true));
        tronaHomeLink.setOnAction(e -> {
            Stage stage = (Stage) tronaHomeLink.getScene().getWindow();
            SceneManager.switchScene(stage, "/org/example/gui/view/HomeView.fxml", "Home Trenical");
        });
    }

    private void modificaBiglietto(boolean conferma) {
        String id = idBigliettoField.getText();
        String data = nuovaDataField.getText();
        String ora = nuovaOraField.getText();
        String classe = nuovaClasseField.getText();
        if (id.isEmpty() || data.isEmpty() || ora.isEmpty() || classe.isEmpty()) {
            risultatoLabel.setText("Compila tutti i campi.");
            return;
        }
        ModificaBigliettoResponse response = client.modificaBiglietto(id, data, ora, classe, conferma);
        if (!response.getSuccess()) {
            risultatoLabel.setText("Errore: " + response.getMessaggio());
        } else {
            risultatoLabel.setText(conferma
                    ? "Modifica confermata. Differenza prezzo: " + response.getDifferenzaPrezzo()
                    : "Simulazione completata. Differenza prezzo: " + response.getDifferenzaPrezzo());
        }
    }
}

