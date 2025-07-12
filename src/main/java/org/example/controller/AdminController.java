package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {

    @FXML
    private Button gestionePromozioniButton;

    @FXML
    private Button gestioneTratteButton;

    @FXML
    private Button tornaAlLoginButton;

    @FXML
    private void initialize() {
        gestionePromozioniButton.setOnAction(event -> apriGestionePromozioni());
        gestioneTratteButton.setOnAction(event -> apriGestioneTratte());
        tornaAlLoginButton.setOnAction(event -> tornaAlLogin());
    }

    private void apriGestionePromozioni() {
        caricaVista("/org/example/gui/view/gestione_promozioni.fxml");
    }

    private void apriGestioneTratte() {
        caricaVista("/org/example/gui/view/gestione_tratte.fxml");
    }

    private void tornaAlLogin() {
        caricaVista("/org/example/gui/view/login.fxml");
    }

    private void caricaVista(String pathFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pathFxml));
            Parent root = loader.load();
            Stage stage = (Stage) gestionePromozioniButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

