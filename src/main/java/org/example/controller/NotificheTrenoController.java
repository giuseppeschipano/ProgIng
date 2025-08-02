package org.example.controller;

import io.grpc.stub.StreamObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.clientgRPC.SceneManager;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.NotificaTrenoResponse;

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
        titoloLabel.setText("Notifiche aggiornate in tempo reale");
        avviaButton.setOnAction(e -> avviaNotificheTreno());
        tornaHomeLink.setOnAction(e ->  {
            Stage stage = (Stage) tornaHomeLink.getScene().getWindow();
            SceneManager.switchScene(stage, "/org/example/gui/view/HomeView.fxml", "Home Trenical");
        });
    }


    private void avviaNotificheTreno() {
        String cf = cfField.getText();
        String idTreno = idTrenoField.getText();

        if (cf.isEmpty() || idTreno.isEmpty()) {
            outputArea.setText("Inserisci codice fiscale e ID treno.");
            return;
        }

        outputArea.appendText("Avvio sottoscrizione alle notifiche..." + "\n");

        client.riceviNotificheTreno(cf, idTreno, new StreamObserver<>() {
            @Override
            public void onNext(NotificaTrenoResponse response) {
                Platform.runLater(() -> {
                    outputArea.appendText(
                            "Stato: " + response.getStato() + "\n" +
                                    "Messaggio: " + response.getMessaggio() + "\n"+
                                    "Orario stimato: " + response.getOrarioStimato() + "\n"
                    );
                });
            }

            @Override
            public void onError(Throwable t) {
                Platform.runLater(() ->
                        outputArea.appendText("Errore: " /* + t.getMessage() */)
                );
            }

            @Override
            public void onCompleted() {
                Platform.runLater(() ->
                        outputArea.appendText("")
                );
            }
        });
    }
}

