package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.AccettiNotificaFedResponse;
import org.example.grpc.PromozioneDTO;

import java.io.IOException;

public class NotifichePromozioniFedeltaController {

    @FXML private TextField cfField;
    @FXML private CheckBox contatoCheckBox;
    @FXML private Button inviaButton;
    @FXML private TextArea risultatoArea;
    @FXML private Hyperlink tornaHomeLink;
    @FXML private Label titoloLabel;

    private final TrenicalClientImpl client = new TrenicalClientImpl("localhost", 50051);

    @FXML
    private void initialize() {
        inviaButton.setOnAction(e -> inviaRichiesta());
        tornaHomeLink.setOnAction(e -> tornaAllaHome());
    }

    private void inviaRichiesta() {
        String cf = cfField.getText().trim();
        boolean desideraContatto = contatoCheckBox.isSelected();

        if (cf.isEmpty()) {
            risultatoArea.setText("Inserisci il codice fiscale per ricevere notifiche.");
            return;
        }

        AccettiNotificaFedResponse response = client.notificaPromoFedelta(cf, desideraContatto);

        StringBuilder messaggio = new StringBuilder();
        messaggio.append(response.getMessage());

        if (response.hasPromoInArrivo()) {
            PromozioneDTO promo = response.getPromoInArrivo();
            messaggio.append("\nPromozione:\n")
                    .append("- Codice: ").append(promo.getCodicePromo()).append("\n")
                    .append("- Sconto: ").append(promo.getPercentualeSconto()).append("%\n")
                    .append("- Tipo treno: ").append(promo.getTipoTreno()).append("\n")
                    .append("- Valida dal ").append(promo.getInizioPromo())
                    .append(" al ").append(promo.getFinePromo()).append("\n")
                    .append("- Solo per fedeltà: ").append(promo.getSoloFedelta() ? "Sì" : "No");
        }

        risultatoArea.setText(messaggio.toString());
    }

    private void tornaAllaHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gui/view/home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tornaHomeLink.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            risultatoArea.setText("Errore nel caricamento della home.");
            e.printStackTrace();
        }
    }
}

