package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.TrattaDTO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CercaTratteController {

    @FXML private TextField partenzaField;
    @FXML private TextField arrivoField;
    @FXML private DatePicker dataPicker;
    @FXML private ComboBox<String> classeCombox;
    @FXML private Button cercaButton;
    @FXML private TableView<TrattaDTO> tableTratte;
    @FXML private TableColumn<TrattaDTO, String> colPartenza;
    @FXML private TableColumn<TrattaDTO, String> colArrivo;
    @FXML private TableColumn<TrattaDTO, String> colOraPartenza;
    @FXML private TableColumn<TrattaDTO, Double> colPrezzo;
    @FXML private TableColumn<TrattaDTO, Integer> colPosti;

    private final TrenicalClientImpl client = new TrenicalClientImpl("localhost", 50051);

    @FXML
    public void initialize() {
        classeCombox.setItems(FXCollections.observableArrayList("ECONOMY", "BUSINESS", "STANDARD", "FIRST"));
        classeCombox.getSelectionModel().selectFirst();

        colPartenza.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStazionePartenza()));
        colArrivo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStazioneArrivo()));
        colOraPartenza.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getOraPartenza()));
        colPrezzo.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPrezzo()).asObject());
        colPosti.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getNumeroPostiDisponibili()).asObject());
        cercaButton.setOnAction(e -> handleRicerca());
    }

    @FXML
    private void handleRicerca() {
        String partenza = partenzaField.getText();
        String arrivo = arrivoField.getText();
        LocalDate data = dataPicker.getValue();
        String classe = classeCombox.getValue();

        if (partenza.isEmpty() || arrivo.isEmpty() || data == null || classe == null) {
            showAlert("Compila tutti i campi per effettuare la ricerca.");
            return;

        }
        String dataString = data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        List<TrattaDTO> risultati = client.cercaTratte(partenza, arrivo, dataString, classe);

        System.out.println("Risultati ricevuti : " + risultati.size());
        for (TrattaDTO t : risultati) {
            System.out.println("Trovata tratta: " + t.getStazionePartenza() + " -> " + t.getStazioneArrivo());
        }

        ObservableList<TrattaDTO> lista = FXCollections.observableArrayList(risultati);
        tableTratte.setItems(lista);
    }

    private void showAlert(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attenzione");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}

