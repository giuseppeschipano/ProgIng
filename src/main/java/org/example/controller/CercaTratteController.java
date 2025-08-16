package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.clientgRPC.SceneManager;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.CercaTratteResponse;
import org.example.grpc.TrattaDTO;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CercaTratteController {

    @FXML private TextField partenzaField;
    @FXML private TextField arrivoField;
    @FXML private DatePicker dataPicker;
    @FXML private ComboBox<String> classeCombox;
    @FXML private Button cercaButton;
    @FXML private Hyperlink tornaHomeLink;
    @FXML private Button prenotaButton;
    @FXML private Button acquistaButton;
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
        prenotaButton.setOnAction(e -> handlePrenota());
        acquistaButton.setOnAction(e -> handleAcquista());
        tornaHomeLink.setOnAction(e -> {
            Stage stage = (Stage) tornaHomeLink.getScene().getWindow();
            SceneManager.switchScene(stage, "/org/example/gui/view/HomeView.fxml", "Home Trenical");
        });
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
        CercaTratteResponse response = client.cercaTratte(partenza, arrivo, dataString, classe);
        List<TrattaDTO> risultati = response.getTratteList();
        ObservableList<TrattaDTO> lista = FXCollections.observableArrayList(risultati);
        tableTratte.setItems(lista);
    }

    @FXML
    private void handlePrenota() {
        TrattaDTO trattaSelezionata = tableTratte.getSelectionModel().getSelectedItem();
        if (trattaSelezionata == null) {
            showAlert("Seleziona una tratta prima di prenotare.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gui/view/PrenotazioneControllerView.fxml"));
            Parent root = loader.load();

            // Il comando passa l'id tratta alla schermata di prenotazione(si evita di ricordare a memoria l'id della tratta cercata quando si effettua una prenotazione della stessa)
            PrenotazioneController controller = loader.getController();
            controller.prefillFromSearch(trattaSelezionata.getIdTratta());
            Stage stage = (Stage) tableTratte.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Prenotazione Biglietto");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert("Impossibile aprire la schermata di prenotazione.");
        }
    }

    @FXML
    private void handleAcquista() {
        TrattaDTO trattaSelezionata = tableTratte.getSelectionModel().getSelectedItem();
        if (trattaSelezionata == null) {
            showAlert("Seleziona una tratta prima di acquistare.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gui/view/AcquistoBigliettoView.fxml"));
            Parent root = loader.load();

            // Il comando passa ID tratta (e la classe selezionata nel filtro) alla schermata di acquisto(si evita di ricordare a memoria l'id della tratta cercata quando si effettua un acquisto della stessa)
            AcquistoController controller = loader.getController();
            controller.prefillFromSearch(trattaSelezionata.getIdTratta(), classeCombox.getValue());
            Stage stage = (Stage) tableTratte.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Acquisto Biglietto");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert("Impossibile aprire la schermata di acquisto.");
        }
    }


    private void showAlert(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attenzione");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}

