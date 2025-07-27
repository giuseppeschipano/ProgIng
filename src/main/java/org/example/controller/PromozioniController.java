package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.clientgRPC.SceneManager;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.PromozioneDTO;

import java.util.List;

public class PromozioniController {

    @FXML private TextField cfField;
    @FXML private Button cercaButton;
    @FXML private TableView<PromozioneDTO> tablePromo;
    @FXML private TableColumn<PromozioneDTO, String> colCodice;
    @FXML private TableColumn<PromozioneDTO, Integer> colSconto;
    @FXML private TableColumn<PromozioneDTO, String> colTipoTreno;
    @FXML private TableColumn<PromozioneDTO, String> colInizio;
    @FXML private TableColumn<PromozioneDTO, String> colFine;
    @FXML private TableColumn<PromozioneDTO, String> colSoloFedelta;
    @FXML private Label esitoLabel;
    @FXML private Hyperlink tornaHomeLink;

    private final TrenicalClientImpl client = new TrenicalClientImpl("localhost", 50051);

    @FXML
    public void initialize() {
        colCodice.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCodicePromo()));
        colSconto.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPercentualeSconto()).asObject());
        colTipoTreno.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipoTreno()));
        colInizio.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getInizioPromo()));
        colFine.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFinePromo()));
        colSoloFedelta.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSoloFedelta() ? "Sì" : "No"));
        cercaButton.setOnAction(e -> cercaPromozioni());
        tornaHomeLink.setOnAction(e -> {
            Stage stage = (Stage) tornaHomeLink.getScene().getWindow();
            SceneManager.switchScene(stage, "/org/example/gui/view/HomeView.fxml", "Home Trenical");
        });
    }

    private void cercaPromozioni() {
        String cf = cfField.getText().trim();
        if (cf.isEmpty()) {
            esitoLabel.setText("Inserisci il codice fiscale.");
            return;
        }

        List<PromozioneDTO> promozioni = client.ottieniPromozioni(cf);
        if (promozioni.isEmpty()) {
            esitoLabel.setText("Nessuna promozione disponibile.");
        } else {
            esitoLabel.setText("Promozioni trovate: " + promozioni.size());
        }

        ObservableList<PromozioneDTO> data = FXCollections.observableArrayList(promozioni);
        tablePromo.setItems(data);
    }
}
