package org.example.controller;

import javafx.fxml.FXML;
import org.example.clientgRPC.SceneManager;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.clientgRPC.TrenicalClientImpl;
import org.example.grpc.AccettiNotificaFedResponse;
import org.example.grpc.PromozioneDTO;

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
        titoloLabel.setText("Benvenuto/a nelle notifiche fedeltà!");
        inviaButton.setOnAction(e -> inviaRichiesta());
        tornaHomeLink.setOnAction(e -> {
            Stage stage = (Stage) tornaHomeLink.getScene().getWindow();
            SceneManager.switchScene(stage, "/org/example/gui/view/HomeView.fxml", "Home Trenical");
        });
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
}

