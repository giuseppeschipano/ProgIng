import org.example.dao.FedeltaDAO;
import org.example.dao.UtenteDAO;
import org.example.model.Fedelta;
import org.example.model.Utente;
import org.example.persistence.DBConnectionSingleton;
import org.example.persistence.PopolaDB;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class FedeltaTest {

    private FedeltaDAO fedeltaDAO;
    private UtenteDAO  utenteDAO;
    private Connection conn;

    @BeforeEach
    public void setUp() throws SQLException {
        PopolaDB.pulisciDatabase();
        this.fedeltaDAO= new FedeltaDAO();
        this.utenteDAO = new UtenteDAO();
        try{
            this.conn = DBConnectionSingleton.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Test che verifica l'aggiunta di una nuova tessera nel database")
    public void testAggiungiTessera() {
        Fedelta fedeltaTest = new Fedelta();
        fedeltaTest.setID("TESSERAFEDELTA2");
        fedeltaTest.setCFPossessoreTessera("BIAALIE98A01T689C");
        fedeltaDAO.aggiungiTessera(fedeltaTest);
        Fedelta tesseraRecuperata = fedeltaDAO.getTesseraByCF("BIAALIE98A01T689C");
        assertTrue(tesseraRecuperata != null);
    }



    @Test
    @DisplayName("Test che controlla la ricerca di una tessera dato il relativo CF")
    public void testGetTesseraByCF() {
        Utente u = utenteDAO.getUtenteByCF("FROMVRD76D09Q739P");
        assertTrue("TESSERAFEDELTA1".equals(u.getCartaUtente().getID()));
    }


    @Test
    @DisplayName("Test che verifica l'incremento dei punti su una tessera fedeltà")
    public void testIncrementaPuntiTessera() {
        String cf = "FROMVRD76D09Q739P";
        Fedelta prima = fedeltaDAO.getTesseraByCF(cf);
        int puntiIniziali = prima.getPunti();
        fedeltaDAO.incrementaPunti(cf, 3);
        Fedelta dopo = fedeltaDAO.getTesseraByCF(cf);
        assertEquals(puntiIniziali + 3, dopo.getPunti());
    }
}

