package org.example.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.clientgRPC.SceneManager;
import org.example.model.Tratta;
import org.example.model.Treno;
import org.example.service.TrattaService;
import org.example.service.TrenoService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AdminController {

    private final TrenoService gestisciTreni = new TrenoService();
    private final TrattaService gestisciTratte = new TrattaService();


    @FXML private Button aggiungiTrenoButton;
    @FXML private Button aggiungiTrattaButton;
    @FXML private Button rimuoviTrenoButton;
    @FXML private Button rimuoviTrattaButton;
    @FXML private Hyperlink tornaHomeLink;


    @FXML private TabPane tabPane;
    @FXML private Tab tabTreni;
    @FXML private Tab tabTratte;


    @FXML private TableView<Treno> treniTable;
    @FXML private TableColumn<Treno, String> colTrenoId;
    @FXML private TableColumn<Treno, String> colTipoTreno;
    @FXML private TableColumn<Treno, String> colStatoTreno;
    @FXML private TableColumn<Treno, Integer> colNumCarrozze;
    @FXML private TextField trenoIdField;
    @FXML private TextField tipoTrenoField;
    @FXML private TextField statoTrenoField;
    @FXML private Spinner<Integer> carrozzeSpinner;
    @FXML private Button btnAggiungiTreno;
    @FXML private Button btnEliminaTreno;
    private final ObservableList<Treno> listaTreni = FXCollections.observableArrayList();


    @FXML private TableView<Tratta> tratteTable;
    @FXML private TableColumn<Tratta, String> colIdTratta;
    @FXML private TableColumn<Tratta, String> colIdTrenoTratta;
    @FXML private TableColumn<Tratta, String> colData;
    @FXML private TableColumn<Tratta, String> colPartenza;
    @FXML private TableColumn<Tratta, String> colArrivo;
    @FXML private TableColumn<Tratta, String> colOraPartenza;
    @FXML private TableColumn<Tratta, String> colOraArrivo;
    @FXML private TableColumn<Tratta, Integer> colPosti;
    @FXML private TableColumn<Tratta, Double> colPrezzo;
    @FXML private TableColumn<Tratta, String> colClassiDisp;
    @FXML private TextField idTrattaField;
    @FXML private TextField idTrenoPerTrattaField;
    @FXML private TextField dataField;
    @FXML private TextField partenzaField;
    @FXML private TextField arrivoField;
    @FXML private TextField oraPartenzaField;
    @FXML private TextField oraArrivoField;
    @FXML private TextField postiField;
    @FXML private TextField prezzoField;
    @FXML private TextField classiDisponibiliField;
    @FXML private Button btnAggiungiTratta;
    @FXML private Button btnEliminaTratta;
    private final ObservableList<Tratta> listaTratte = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        aggiungiTrenoButton.setOnAction(e -> tabPane.getSelectionModel().select(tabTreni));
        rimuoviTrenoButton.setOnAction(e -> tabPane.getSelectionModel().select(tabTreni));
        aggiungiTrattaButton.setOnAction(e -> tabPane.getSelectionModel().select(tabTratte));
        rimuoviTrattaButton.setOnAction(e -> tabPane.getSelectionModel().select(tabTratte));
        tornaHomeLink.setOnAction(e -> {
            Stage stage = (Stage) tornaHomeLink.getScene().getWindow();
            SceneManager.switchScene(stage, "/org/example/gui/view/HomeView.fxml", "Home Trenical");
        });

        carrozzeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1));
        colTrenoId.setCellValueFactory(new PropertyValueFactory<>("id_Treno"));
        colTipoTreno.setCellValueFactory(new PropertyValueFactory<>("tipologia"));
        colStatoTreno.setCellValueFactory(new PropertyValueFactory<>("stato"));
        colNumCarrozze.setCellValueFactory(new PropertyValueFactory<>("carrozza"));
        treniTable.setItems(listaTreni);
        btnAggiungiTreno.setOnAction(e -> handleAggiungiTreno());
        btnEliminaTreno.setOnAction(e -> handleEliminaTreno());
        colIdTratta.setCellValueFactory(new PropertyValueFactory<>("id_tratta"));
        colIdTrenoTratta.setCellValueFactory(new PropertyValueFactory<>("id_treno"));


        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        colData.setCellValueFactory(data -> {
            String rawDate = data.getValue().getData();
            try {
                LocalDate parsed = LocalDate.parse(rawDate);
                return new SimpleStringProperty(parsed.format(dateFormatter));
            } catch (Exception ex) {
                return new SimpleStringProperty(rawDate);
            }
        });

        colPartenza.setCellValueFactory(new PropertyValueFactory<>("stazionePartenza"));
        colArrivo.setCellValueFactory(new PropertyValueFactory<>("stazioneArrivo"));


        colOraPartenza.setCellValueFactory(data -> {
            String rawTime = data.getValue().getOraPartenza();
            try {
                LocalTime parsed = LocalTime.parse(rawTime);
                return new SimpleStringProperty(parsed.format(timeFormatter));
            } catch (Exception ex) {
                return new SimpleStringProperty(rawTime);
            }
        });

        colOraArrivo.setCellValueFactory(data -> {
            String rawTime = data.getValue().getOraArrivo();
            try {
                LocalTime parsed = LocalTime.parse(rawTime);
                return new SimpleStringProperty(parsed.format(timeFormatter));
            } catch (Exception ex) {
                return new SimpleStringProperty(rawTime);
            }
        });

        colPosti.setCellValueFactory(new PropertyValueFactory<>("numeroPostiDisponibili"));
        colPrezzo.setCellValueFactory(new PropertyValueFactory<>("prezzo"));
        colClassiDisp.setCellValueFactory(new PropertyValueFactory<>("classiDisponibili"));
        tratteTable.setItems(listaTratte);
        btnAggiungiTratta.setOnAction(e -> handleAggiungiTratta());
        btnEliminaTratta.setOnAction(e -> handleEliminaTratta());
    }



    private void handleAggiungiTreno() {
        String id = trenoIdField.getText();
        String tipo = tipoTrenoField.getText();
        String stato = statoTrenoField.getText();
        Integer carrozze = carrozzeSpinner.getValue();
        if (id.isEmpty() || tipo.isEmpty() || stato.isEmpty()) {
            showAlert("Compila tutti i campi del treno.");
            return;
        }
        Treno treno = new Treno(id, tipo, stato, carrozze);
        gestisciTreni.aggiungiTreno(treno);
        listaTreni.setAll(gestisciTreni.getTuttiTreni());
        clearTrenoForm();
    }

    private void handleEliminaTreno() {
        Treno tr = treniTable.getSelectionModel().getSelectedItem();
        if (tr == null) return;
        gestisciTreni.rimuoviTreno(tr);
        listaTreni.setAll(gestisciTreni.getTuttiTreni());

    }
    private void clearTrenoForm() {
        trenoIdField.clear();
        tipoTrenoField.clear();
        statoTrenoField.clear();
        carrozzeSpinner.getValueFactory().setValue(1);
    }


    @FXML
    private void handleAggiungiTratta(){
        Tratta tratta = new Tratta();
        tratta.setNumeroPostiDisponibili(Integer.parseInt(postiField.getText()));
        tratta.setId_tratta(idTrattaField.getText());
        tratta.setId_treno(idTrenoPerTrattaField.getText());
        tratta.setOraPartenza(oraPartenzaField.getText());
        tratta.setOraArrivo(oraArrivoField.getText());
        tratta.setStazionePartenza(partenzaField.getText());
        tratta.setStazioneArrivo(arrivoField.getText());
        tratta.setClassiDisponibili(classiDisponibiliField.getText());
        tratta.setData(dataField.getText());
        tratta.setPrezzo(Double.parseDouble(prezzoField.getText()));
        gestisciTratte.aggiungiTratta(tratta);
        listaTratte.setAll(gestisciTratte.getAllTratte());
        clearTrattaForm();
    }

    private void handleEliminaTratta() {
        Tratta tt = tratteTable.getSelectionModel().getSelectedItem();
        if (tt == null) return;
        gestisciTratte.rimuoviTratta(tt.getId_tratta());
        listaTratte.setAll(gestisciTratte.getAllTratte());
    }

    private void clearTrattaForm() {
        idTrattaField.clear();
        idTrenoPerTrattaField.clear();
        dataField.clear();
        partenzaField.clear();
        arrivoField.clear();
        oraPartenzaField.clear();
        oraArrivoField.clear();
        postiField.clear();
        prezzoField.clear();
    }


    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
