package org.example.controller;

import io.grpc.stub.StreamObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.NotificaTrenoResponse;
import java.io.IOException;

public class NotificheTrenoController {

    @FXML private Label titoloLabel;
    @FXML private TextField cfField;
    @FXML private TextField idTrenoField;
    @FXML private Button avviaButton;
    @FXML private TextArea outputArea;
    @FXML private Hyperlink tornaHomeLink;

    private final TrenicalClientImpl client = new TrenicalClientImpl("localhost", 50051);

    @FXML
    public void initialize() {
        avviaButton.setOnAction(e -> avviaNotificheTreno());
        tornaHomeLink.setOnAction(e -> tornaAllaHome());
    }

    private void avviaNotificheTreno() {
        String cf = cfField.getText();
        String idTreno = idTrenoField.getText();

        if (cf.isEmpty() || idTreno.isEmpty()) {
            outputArea.setText("Inserisci codice fiscale e ID treno.");
            return;
        }

        outputArea.appendText("Avvio sottoscrizione alle notifiche...");

        client.riceviNotificheTreno(cf, idTreno, new StreamObserver<>() {
            @Override
            public void onNext(NotificaTrenoResponse response) {
                Platform.runLater(() -> {
                    outputArea.appendText(
                            "Stato: " + response.getStato() +
                                    "Messaggio: " + response.getMessaggio() +
                                    "Orario stimato: " + response.getOrarioStimato()
                    );
                });
            }

            @Override
            public void onError(Throwable t) {
                Platform.runLater(() ->
                        outputArea.appendText("Errore: " + t.getMessage() )
                );
            }

            @Override
            public void onCompleted() {
                Platform.runLater(() ->
                        outputArea.appendText("Connessione chiusa dal server.")
                );
            }
        });
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

