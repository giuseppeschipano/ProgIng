package org.example.service;

import org.example.model.Tratta;
import org.example.dao.TrattaDAO;
import org.example.persistence.DBConnectionSingleton;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TrattaService {

    private final TrattaDAO trattaRepository;

    public TrattaService() {
        this.trattaRepository =  new TrattaDAO();
    }


    public List<Tratta> cercaTratte(String stazionePartenza, String stazioneArrivo, String dataViaggio, String classeDesiderata){
        return  trattaRepository.cercaTratta(stazionePartenza,stazioneArrivo,dataViaggio,classeDesiderata);
    }

    public void decrementaPostiDisponibili(String idTratta, int quanti) {
        try {
            Connection conn = DBConnectionSingleton.getConnection();
            Tratta tratta = trattaRepository.getTrattaById(idTratta);
            if (tratta != null && tratta.getNumeroPostiDisponibili() >= quanti) {
                int nuoviPosti = tratta.getNumeroPostiDisponibili() - quanti;
                trattaRepository.updatePostiDisponibili(idTratta, nuoviPosti);
            } else {
                System.out.println("Errore: tratta non trovata o posti insufficienti");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPostiDisponibili(String idTratta){
        Connection conn = null;
        try  {
            conn = DBConnectionSingleton.getConnection();
            Tratta tratta = trattaRepository.getTrattaById(idTratta);
            if(tratta != null && tratta.getNumeroPostiDisponibili() > 0){
                return tratta.getNumeroPostiDisponibili();
            }else{
                return 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void incrementaPostiDisponibili(String idTratta, int quanti) {
        try (Connection conn = DBConnectionSingleton.getConnection()) {
            Tratta tratta = trattaRepository.getTrattaById(idTratta);
            if (tratta != null) {
                int nuoviPosti = tratta.getNumeroPostiDisponibili() + quanti;
                trattaRepository.updatePostiDisponibili(idTratta, nuoviPosti);
            } else {
                System.out.println("Errore: tratta non trovata");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Tratta getTrattaByID(String idTratta) throws SQLException {
        return trattaRepository.getTrattaById(idTratta);
    }

    public List<Tratta> getAllTratte() {
        return trattaRepository.getAllTratte();
    }

    public void aggiungiTratta(Tratta tratta) {
        trattaRepository.aggiungiTratta(tratta);
    }

    public  void rimuoviTratta(String idTratta) {
        trattaRepository.rimuoviTratta(idTratta);
    }
}
