package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.clientgRPC.SceneManager;

public class AdminController {

    @FXML
    private Button gestionePromozioniButton;

    @FXML
    private Button gestioneTratteButton;

    @FXML
    private Button tornaAlLoginButton;

    @FXML
    private void initialize() {
        gestionePromozioniButton.setOnAction(event -> {
            Stage stage = (Stage) gestionePromozioniButton.getScene().getWindow();
            SceneManager.switchScene(stage, "/org/example/gui/view/gestione_promozioni.fxml", "Gestione Promozioni");
        });
        gestioneTratteButton.setOnAction(event -> {
            Stage stage = (Stage) gestioneTratteButton.getScene().getWindow();
            SceneManager.switchScene(stage, "/org/example/gui/view/gestione_tratte.fxml", "Gestione Tratte");
        });
        tornaAlLoginButton.setOnAction(event -> {
            Stage stage = (Stage) tornaAlLoginButton.getScene().getWindow();
            SceneManager.switchScene(stage, "/org/example/gui/view/LoginView.fxml", "Login");
        });
    }
}


