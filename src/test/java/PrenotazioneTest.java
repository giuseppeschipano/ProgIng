import org.example.dao.PrenotazioneDAO;
import org.example.model.Prenotazione;
import org.example.persistence.DBConnectionSingleton;
import org.example.persistence.PopolaDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PrenotazioneTest {

    private PrenotazioneDAO prenotazioneDAO;
    private Connection con;
    private final String idPrenotazioneTest = "PRE2549735126794";

    @BeforeEach
    public void setUp() throws SQLException {
        PopolaDB.pulisciDatabase();
        this.prenotazioneDAO= new PrenotazioneDAO();
        try{
            this.con = DBConnectionSingleton.getConnection();
            Prenotazione prenotazione = new Prenotazione();
            prenotazione.setId_Prenotazione(idPrenotazioneTest);
            prenotazione.setPostoPrenotazione(22);
            prenotazione.setDataScadenza("14/07/2025");
            prenotazione.setCarrozza(1);
            prenotazione.setId_tratta("TRATTA3");
            prenotazione.setCFUtente("BIAALIE98A01T689C");
            prenotazioneDAO.aggiungiPrenotazione(prenotazione, con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @DisplayName("Test che verifica l'aggiunta di una nuova prenotazione nel database")
    public void testAggiungiPrenotazione() {
        Prenotazione prenotazioneTest = new Prenotazione();
        String idPrenotazione = "PRE" + System.currentTimeMillis();
        prenotazioneTest.setId_Prenotazione(idPrenotazione);
        prenotazioneTest.setCarrozza(4);
        prenotazioneTest.setCFUtente("SCHGPP04A03F537C");
        prenotazioneTest.setPostoPrenotazione(17);
        prenotazioneTest.setId_tratta("TRATTA3");
        prenotazioneTest.setDataScadenza("14/07/2025");
        prenotazioneDAO.aggiungiPrenotazione(prenotazioneTest,con);
        Prenotazione prenotazioneRecuperata = prenotazioneDAO.getPrenotazionePerID(idPrenotazione);
        assertTrue(prenotazioneRecuperata != null);

    }

    @Test
    @DisplayName("Test che controlla la ricerca di una prenotazione dato il relativo ID")
    public void checkTrovaPrenotazioneDaId() {
        Prenotazione prenotazione = prenotazioneDAO.getPrenotazionePerID(idPrenotazioneTest);
        assertNotNull(prenotazione);
    }

    @Test
    @DisplayName("Test che controlla le prenotazioni di un utente dato il CF")
    public void checkTrovaPrenotazionePerUtente() {
        List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazionePerUtente("BIAALIE98A01T689C");
        assertFalse(prenotazioni.isEmpty());
    }

    @Test
    @DisplayName("Test che verifica l'esistenza delle prenotazioni nel DB")
    public void checkEsistenzaPrenotazioni() {
        List<Prenotazione> ris = prenotazioneDAO.getTuttePrenotazioni();
        assertFalse(ris.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"PRE2549735126794"})
    @DisplayName("Test che verifica la rimozione di una prenotazione dal database")
    public void testRimuoviPrenotazione(String id_prenotazione) {
        prenotazioneDAO.rimuoviPrenotazione(id_prenotazione, con);
        Prenotazione eliminata = prenotazioneDAO.getPrenotazionePerID(id_prenotazione);
        assertTrue(eliminata == null);

    }
}
