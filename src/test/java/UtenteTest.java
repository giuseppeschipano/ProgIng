import org.example.dao.UtenteDAO;
import org.example.model.Utente;
import org.example.persistence.DBConnectionSingleton;
import org.example.persistence.PopolaDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class UtenteTest {

    private UtenteDAO utenteDAO;
    private Connection con;

    @BeforeEach
    public void setUp() throws SQLException {
        PopolaDB.pulisciDatabase();
        this.utenteDAO = new UtenteDAO();
        try {
            this.con = DBConnectionSingleton.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Test che verifica l'aggiunta di un nuovo utente nel database")
    public void testAggiungiUtente() {
        Utente utenteTest = new Utente();
        utenteTest.setNomeUtente("Mario");
        utenteTest.setCognomeUtente("Rossi");
        utenteTest.setDataNascitaUtente("01/01/2005");
        utenteTest.setIndirizzoUtente("Italia");
        utenteTest.setCFUtente("ROSMRIO05A01H948D");
        utenteTest.setEmailUtente("mariorossi05@gmail.com");
        utenteTest.setPasswordUtente("mariorossi0105");
        utenteTest.setAdmin(false);
        utenteTest.setCartaUtente(null);
        utenteDAO.aggiungiUtente(utenteTest);
        Utente utenteRecuperato = utenteDAO.getUtenteByCF("ROSMRIO05A01H948D");
        assertTrue(utenteRecuperato != null);
    }

    @ParameterizedTest
    @ValueSource(strings = {"BIAALIE98A01T689C"})
    @DisplayName("Test che controlla la ricerca di un utente dato il relativo cf")
    public void checkTovaUtenteDaCF(String cf) {
        Utente ris = utenteDAO.getUtenteByCF(cf);
        assertTrue(ris != null);

    }

    @ParameterizedTest
    @ValueSource(strings = {"alicebianchi98@gmail.com"})
    @DisplayName("Test che controlla la ricerca di un utente dato il relativo indirizzo email")
    public void checkTovaUtenteDaEmail(String email) {
        Utente ris = utenteDAO.getUtenteByEmail(email);
        assertTrue(ris != null);
    }

    @Test
    @DisplayName("Test aggiornamento carta fedeltà utente")
    public void testUpdateFedelta() {

        //Verifico che inizialmente l'utente non abbia id_fedelta
        Utente utentePrima = utenteDAO.getUtenteByCF("SEATEPT47D09R739C");
        assertNull(utentePrima.getCartaUtente());

        utenteDAO.updateFedelta("SEATEPT47D09R739C", "TESSERAFEDELTA3");

        //Verifico che il campo id_fedelta sia stato aggiornato dopo aver fatto update
        Utente utenteDopo = utenteDAO.getUtenteByCF("SEATEPT47D09R739C");
        assertNotNull(utenteDopo.getCartaUtente());

        assertEquals("TESSERAFEDELTA3", utenteDopo.getCartaUtente().getID());
    }
}
