package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import org.example.clientgRPC.SceneManager;

public class HomeController {

    @FXML private Button cercaTratteButton;
    @FXML private Button prenotaButton;
    @FXML private Button acquistaButton;
    @FXML private Button fedeltaButton;
    @FXML private Button promoButton;
    @FXML private Button modificaButton;
    @FXML private Button notificheButton;
    @FXML private Hyperlink logoutLink;
    @FXML private Button statoTrenoButton;
    @FXML private Button notifichePromoFedeltaButton;


    @FXML
    public void initialize() {
        cercaTratteButton.setOnAction(e -> cambiaScena("CercaTratteView.fxml", "Cerca Tratte"));
        prenotaButton.setOnAction(e -> cambiaScena("PrenotazioneControllerView.fxml", "Prenotazione"));
        acquistaButton.setOnAction(e -> cambiaScena("AcquistoBigliettoView.fxml", "Acquisto Biglietto"));
        fedeltaButton.setOnAction(e -> cambiaScena("SottoscriviFedeltaView.fxml", "Sottoscrizione Fedeltà"));
        promoButton.setOnAction(e -> cambiaScena("OttieniPromozioniView.fxml", "Promozioni"));
        modificaButton.setOnAction(e -> cambiaScena("ModificaBigliettoView.fxml", "Modifica Biglietto"));
        notificheButton.setOnAction(e -> cambiaScena("NotificheTrenoView.fxml", "Notifiche"));
        logoutLink.setOnAction(e -> cambiaScena("LoginView.fxml", "Login"));
        statoTrenoButton.setOnAction(e -> cambiaScena("StatoTrenoView.fxml", "Stato Treno"));
        notifichePromoFedeltaButton.setOnAction(e -> cambiaScena("NotificheFedeltaView.fxml", "Notifiche Promo"));
    }

    private void cambiaScena(String fxml, String titolo) {
        Stage stage = (Stage) cercaTratteButton.getScene().getWindow();
        SceneManager.switchScene(stage, "/org/example/gui/view/" + fxml, titolo);
    }
}

