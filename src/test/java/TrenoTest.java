
import org.example.model.Treno;
import org.example.persistence.DBConnectionSingleton;
import org.example.dao.TrenoDAO;
import org.example.persistence.PopolaDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TrenoTest {

    private TrenoDAO trenoDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        PopolaDB.pulisciDatabase();
        this.trenoDAO = new TrenoDAO();
        try{
            this.connection = DBConnectionSingleton.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Test che verifica l'aggiunta di un nuovo Treno nel database")
    public void testAggiungiTreno() {
        Treno trenoTest = new Treno();
        trenoTest.setId_Treno("FR-1234");
        trenoTest.setTipologia("FRECCIAROSSA");
        trenoTest.setStato("IN VIAGGIO");
        trenoTest.setCarrozza(8);

        trenoDAO.aggiungiTreno(trenoTest);

        Treno trenoRecuperato = trenoDAO.getTrenoById("FR-1234");
        assertTrue(trenoRecuperato != null);
    }


    @ParameterizedTest
    @ValueSource(strings =  {"EC-7893"})
    @DisplayName("Test che controlla la ricerca di un Treno dato un ID")
    public void checkTovaTreno(String id){
        Treno ris = trenoDAO.getTrenoById(id);
        assertTrue (ris != null);

    }

    @Test
    @DisplayName("Test che verifica l'esistenza dei treni nel DB")
    public void checkEsistenzaTreni(){
        List<Treno> ris = trenoDAO.getAllTreni();
        assertFalse(ris.isEmpty());
    }
}


