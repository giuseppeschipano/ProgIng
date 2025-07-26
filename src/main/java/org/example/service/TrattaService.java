package org.example.service;

import org.example.model.Tratta;
import org.example.dao.TrattaDAO;
import org.example.persistence.DBConnectionSingleton;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrattaService {

    private final TrattaDAO trattaRepository;

    public TrattaService(TrattaDAO trattaRepository) {
        this.trattaRepository = trattaRepository;
    }

    // Ricerca tratte secondo criteri classici
    public List<Tratta> cercaTratte(String stazionePartenza, String stazioneArrivo, String dataViaggio, String classeDesiderata) {
        List<Tratta> tratteTrovate = new ArrayList<>();
        List<Tratta> tutte = trattaRepository.getAllTratte();

        for (Tratta t : tutte) {
            boolean matchPartenza = t.getStazionePartenza().equals(stazionePartenza);
            boolean matchArrivo = t.getStazioneArrivo().equals(stazioneArrivo);
            boolean matchData = t.getOraPartenza().contains(dataViaggio); // Controllo solo la parte data
            boolean matchClasse = (classeDesiderata == null || t.getClassiDisponibili().contains(classeDesiderata));

            if (matchPartenza && matchArrivo && matchData && matchClasse) {
                tratteTrovate.add(t);
            }
        }

        return tratteTrovate;
    }

    public void decrementaPostiDisponibili(String idTratta, int quanti) {
        try (Connection conn = DBConnectionSingleton.getConnection()) {
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
        try (Connection conn = DBConnectionSingleton.getConnection()) {
            Tratta tratta = trattaRepository.getTrattaById(idTratta);
            if(tratta != null && tratta.getNumeroPostiDisponibili() > 0){
                return tratta.getNumeroPostiDisponibili();
            }else{
                return 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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

    public Tratta getTrattaByID(String idTratta) {
        return trattaRepository.getTrattaById(idTratta);
    }

    public List<Tratta> getAllTratte() {
        return trattaRepository.getAllTratte();
    }
}
