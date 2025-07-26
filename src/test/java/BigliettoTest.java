import org.example.dao.BigliettoDAO;
import org.example.model.Biglietto;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BigliettoTest {

    private BigliettoDAO bigliettoDAO;
    private Connection conn;

    @BeforeEach
    public void setUp() throws SQLException {
        PopolaDB.pulisciDatabase();
        this.bigliettoDAO= new BigliettoDAO();
        try{
            this.conn = DBConnectionSingleton.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Test che verifica l'aggiunta di una biglietto nel database")
    public void testAggiungiBigliettoDB() throws SQLException {
        Biglietto bigliettoTest = new Biglietto();
        bigliettoTest.setId_Biglietto("IC1482349576130");
        bigliettoTest.setCarrozza(3);
        bigliettoTest.setCF("SCHGPP04A03F537C");
        bigliettoTest.setPosto(2);
        bigliettoTest.setClasse("SECONDA CLASSE");
        bigliettoTest.setId_tratta("TRATTA4");
        bigliettoTest.setId_prenotazione(null);
        bigliettoDAO.aggiungiBiglietto(bigliettoTest);
        Biglietto bigliettoRecuperato = bigliettoDAO.getBigliettoPerID("IC1482349576130");
        assertTrue(bigliettoRecuperato != null);
    }

    @ParameterizedTest
    @ValueSource(strings =  {"FA1750265026411"})
    @DisplayName("Test che controlla la ricerca di un biglietto dato un ID")
    public void checkTovaBiglietto(String id){
        Biglietto ris = bigliettoDAO.getBigliettoPerID(id);
        assertTrue (ris != null);
    }

    @ParameterizedTest
    @ValueSource(strings =  {"BIAALIE98A01T689C"})
    @DisplayName("Test che controlla la ricerca di un biglietto dato un cf")
    public void checkTovaBigliettoDatoCF(String cf){
        List<Biglietto> ris = bigliettoDAO.getBigliettiPerUtente(cf);
        assertFalse (ris.isEmpty());
    }

    @Test
    @DisplayName("Test aggiornamento biglietto nel database")
    public void testAggiornaBiglietto()  {
        Biglietto biglietto = bigliettoDAO.getBigliettoPerID("FA1750265026411");
        biglietto.setClasse("PRIMA CLASSE");
        biglietto.setCarrozza(2);
        biglietto.setPosto(12);
        bigliettoDAO.aggiornaBiglietto(biglietto);
        Biglietto aggiornato = bigliettoDAO.getBigliettoPerID("FA1750265026411");
        assertTrue(aggiornato.getPosto() == 12);
    }
}
