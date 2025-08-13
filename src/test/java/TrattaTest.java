import org.example.dao.TrattaDAO;
import org.example.model.Tratta;
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

public class TrattaTest {

    private TrattaDAO trattaDAO;
    private Connection con;

    @BeforeEach
    public void setUp() throws SQLException {
        PopolaDB.pulisciDatabase();
        this.trattaDAO= new TrattaDAO();
        try{
            this.con = DBConnectionSingleton.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @DisplayName("Test che verifica l'aggiunta di una nuova tratta nel database")
    public void testAggiungiTratta() throws SQLException {
        Tratta trattaTest = new Tratta();
        trattaTest.setId_tratta("TRATTA1");
        trattaTest.setId_treno("EC-7893");
        trattaTest.setData("22/07/2025");
        trattaTest.setOraPartenza("15:00:00");
        trattaTest.setOraArrivo("16:15:00");
        trattaTest.setStazionePartenza("Castiglione Cosentino");
        trattaTest.setStazioneArrivo("Vibo Pizzo");
        trattaTest.setNumeroPostiDisponibili(60);
        trattaTest.setClassiDisponibili("ECONOMY");
        trattaTest.setPrezzo(8.50);
        trattaDAO.aggiungiTratta(trattaTest);
        Tratta trattaRecuperata = trattaDAO.getTrattaByID("TRATTA1");
        assertTrue(trattaRecuperata != null);
    }



    @Test
    @DisplayName("Test che verifica l'esistenza delle tratte nel DB")
    public void checkEsistenzaTratte(){
        List<Tratta> ris = trattaDAO.getAllTratte();
        assertFalse(ris.isEmpty());
    }

    @Test
    @DisplayName("Test che controlla la ricerca di una tratta dati partenza, arrivo, data e tipo treno")
    public void checkTrovaTrattaDaParametri() {
        List<Tratta> ris = trattaDAO.cercaTratta("Gioia Tauro", "Cosenza", "12/05/2025", "FIRST");
        assertFalse(ris.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings =  {"TRATTA2"})
    @DisplayName("Test che controlla l'esistenza di una tratta dato il relativo id")
    public void checkTovaTrattaDaID(String id_tratta) throws SQLException {
        Tratta ris = trattaDAO.getTrattaById(id_tratta);
        assertTrue(ris != null);

    }

    @ParameterizedTest
    @ValueSource(strings =  {"TRATTA2"})
    @DisplayName("Test che verifica che il numero di posti disponibili di una tratta sia maggiore di 0 ")
    public void checkDammiNumeroPostiDaID(String id_tratta){
        int ris = trattaDAO.getPostiDisponibili(id_tratta);
        assertTrue(ris > 0);
    }


    @Test
    @DisplayName("Test incremento posti disponibili")
    public void testIncrementaPostiDisponibili() throws SQLException {
        TrattaDAO trattaDAO = new TrattaDAO();
        trattaDAO.updatePostiDisponibili("TRATTA2", 10); // reset

        // Incremento di 3
        trattaDAO.incrementaPostiDisponibili( "TRATTA2", 3);

        // Verifica che ora siano 13
        Tratta trattaAggiornata = trattaDAO.getTrattaById("TRATTA2");
        assertEquals(13, trattaAggiornata.getNumeroPostiDisponibili());
    }

    @Test
    @DisplayName("Test decremento posti disponibili")
    public void testDecrementaPostiDisponibili() throws SQLException {
        TrattaDAO trattaDAO = new TrattaDAO();

        // Decremento di 3
        trattaDAO.decrementaPostiDisponibili( "TRATTA3", 3);

        // Verifica che ora siano 40
        Tratta trattaAggiornata = trattaDAO.getTrattaById("TRATTA3");
        assertEquals(40, trattaAggiornata.getNumeroPostiDisponibili());
    }

    @ParameterizedTest
    @ValueSource(strings =  {"REG-5567"})
    @DisplayName("Test che controlla l'esistenza di una tratta dato l'id di un treno che percorre la stessa ")
    public void checkDammiTrattaDaIDTreno(String id_treno){
        Tratta ris = trattaDAO.getTrattaByIdTreno(id_treno);
        assertTrue(ris != null);
    }

    @Test
    @DisplayName("Test che verifica l'aggiornamento di una tratta esistente nel database")
    public void testUpdateTratta() throws SQLException {
        Tratta trattaDaAggiornare = trattaDAO.getTrattaById("TRATTA2");
        trattaDaAggiornare.setOraPartenza("09:34:00");
        trattaDaAggiornare.setOraArrivo("11:42:00");
        trattaDaAggiornare.setPrezzo(39.10);
        trattaDaAggiornare.setNumeroPostiDisponibili(50);
        trattaDaAggiornare.setClassiDisponibili("ECONOMY");
        trattaDAO.updateTratta(trattaDaAggiornare);
        Tratta trattaAggiornata = trattaDAO.getTrattaById("TRATTA2");
        assertEquals("09:34:00", trattaAggiornata.getOraPartenza());
        assertEquals("11:42:00", trattaAggiornata.getOraArrivo());
        assertEquals(39.10, trattaAggiornata.getPrezzo());
        assertEquals(50, trattaAggiornata.getNumeroPostiDisponibili());
        assertEquals("ECONOMY", trattaAggiornata.getClassiDisponibili());
    }
}
