import org.example.dao.PromozioneDAO;
import org.example.model.Promozione;
import org.example.persistence.DBConnectionSingleton;
import org.example.persistence.PopolaDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import  java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PromozioneTest {

    private PromozioneDAO promozioneDAO;
    private Connection con;

    @BeforeEach
    public void setUp() throws SQLException {
        PopolaDB.pulisciDatabase();
        this.promozioneDAO= new PromozioneDAO();
        try{
            this.con = DBConnectionSingleton.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Test che verifica l'aggiunta di una nuova promozione nel database")
    public void testAggiungiPromozione() {
       Promozione promozioneTest = new Promozione();
       promozioneTest.setCodicePromo("PROMO1");
            promozioneTest.setTipoTreno("ECONOMY");
            promozioneTest.setInizioPromo("12/07/2025");
            promozioneTest.setFinePromo("12/08/2025");
            promozioneTest.setPercentualeSconto(15);
            promozioneTest.setSoloFedelta(false);

            try (Connection conn = DBConnectionSingleton.getConnection()) {
            promozioneDAO.addPromozione(promozioneTest);
            for (Promozione promoRecuperata: promozioneDAO.getAllPromozioni()){
                assertTrue(promoRecuperata != null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test che verifica l'esistenza di promozioni attive nel DB")
    public void checkEsistenzaPromozioniAttive() {
        List<Promozione> ris = promozioneDAO.getAllPromozioni();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Promozione> promozioniAttive = new ArrayList<>();
        for (Promozione promo : ris) {
            LocalDate inizio = LocalDate.parse(promo.getInizioPromo(), formatter);
            LocalDate fine = LocalDate.parse(promo.getFinePromo(), formatter);
            if ((LocalDate.now().isAfter(inizio) || LocalDate.now().isEqual(inizio)) && (LocalDate.now().isBefore(fine) || LocalDate.now().isEqual(fine))) {
                promozioniAttive.add(promo);
            }
        }
        assertFalse(promozioniAttive.isEmpty());
    }


    @Test
    @DisplayName("Test che verifica l'esistenza delle promozioni nel DB")
    public void checkEsistenzaPromozioni(){
        List<Promozione> ris = promozioneDAO.getAllPromozioni();
        assertFalse(ris.isEmpty());
    }
}
