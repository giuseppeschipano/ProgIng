package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.ModificaBigliettoResponse;
import java.io.IOException;

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
        tronaHomeLink.setOnAction(e -> tornaAllaHome());
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
            if (conferma) {
                risultatoLabel.setText("Modifica confermata. Differenza prezzo: " + response.getDifferenzaPrezzo());
            } else {
                risultatoLabel.setText("Simulazione avvenuta con successo. Differenza prezzo: " + response.getDifferenzaPrezzo());
            }
        }
    }

    private void tornaAllaHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gui/view/home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tronaHomeLink.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

