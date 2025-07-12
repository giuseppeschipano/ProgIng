package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

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
        cercaTratteButton.setOnAction(e -> caricaView("cercaTratte.fxml"));
        prenotaButton.setOnAction(e -> caricaView("prenotazione.fxml"));
        acquistaButton.setOnAction(e -> caricaView("acquisto.fxml"));
        fedeltaButton.setOnAction(e -> caricaView("fedelta.fxml"));
        promoButton.setOnAction(e -> caricaView("promozioni.fxml"));
        modificaButton.setOnAction(e -> caricaView("modifica.fxml"));
        notificheButton.setOnAction(e -> caricaView("notifiche.fxml"));
        logoutLink.setOnAction(e -> caricaView("login.fxml"));
        notifichePromoFedeltaButton.setOnAction(e -> apriNotifichePromozioniFedelta());


    }

    private void caricaView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gui/view/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) cercaTratteButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void vaiAStatoTreno() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gui/view/statoTreno.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) statoTrenoButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void apriNotifichePromozioniFedelta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gui/view/notifichePromozioniFedelta.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) notifichePromoFedeltaButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

