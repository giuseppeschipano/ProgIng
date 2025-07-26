package org.example.service;

import org.example.model.Biglietto;
import org.example.model.Prenotazione;
import org.example.dao.BigliettoDAO;
import org.example.dao.PrenotazioneDAO;
import org.example.dao.TrattaDAO;
import org.example.persistence.DBConnectionSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PrenotazioneService {

    private final PrenotazioneDAO prenotazioneRepo;
    private final BigliettoDAO bigliettoRepo;
    private final TrattaDAO trattaRepo;

    public PrenotazioneService(PrenotazioneDAO prenotazioneRepo, BigliettoDAO bigliettoRepo, TrattaDAO trattaRepo) {
        this.prenotazioneRepo = prenotazioneRepo;
        this.bigliettoRepo = bigliettoRepo;
        this.trattaRepo = trattaRepo;
    }

    public void effettuaPrenotazione(Prenotazione p) {
        try (Connection conn = DBConnectionSingleton.getConnection()) {
            conn.setAutoCommit(false);

            int postiDisponibili = trattaRepo.getPostiDisponibili(p.getId_tratta());
            if (postiDisponibili <= 0) {
                System.out.println("Treno pieno. Impossibile prenotare.");
                return;
            }

            prenotazioneRepo.aggiungiPrenotazione(p);
            trattaRepo.decrementaPostiDisponibili(p.getId_tratta(), 1);

            conn.commit();
            System.out.println("Prenotazione effettuata con successo.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void confermaPrenotazione(Prenotazione p, Biglietto b) {
        try (Connection conn = DBConnectionSingleton.getConnection()) {
            conn.setAutoCommit(false);

            bigliettoRepo.aggiungiBiglietto(b);
            prenotazioneRepo.rimuoviPrenotazione(p.getId_Prenotazione());

            conn.commit();
            System.out.println("Prenotazione confermata e biglietto emesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void controllaPrenotazioniScadute() {
        List<Prenotazione> tutte = prenotazioneRepo.getTuttePrenotazioni();
        LocalDateTime oraCorrente = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        for (Prenotazione p : tutte) {
            try {
                LocalDateTime scadenza = LocalDateTime.parse(p.getDataScadenza(), formatter);
                if (oraCorrente.isAfter(scadenza)) {
                    annullaPrenotazione(p);
                }
            } catch (Exception e) {
                System.err.println("Errore nel parsing della data: " + p.getDataScadenza());
            }
        }
    }


    public void annullaPrenotazione(Prenotazione p) {
        try (Connection conn = DBConnectionSingleton.getConnection()) {
            conn.setAutoCommit(false);

            prenotazioneRepo.rimuoviPrenotazione(p.getId_Prenotazione());
            trattaRepo.incrementaPostiDisponibili(p.getId_tratta(), 1);

            conn.commit();
            System.out.println("Prenotazione " + p.getId_Prenotazione() + " annullata. Posto liberato.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean postoGiaOccupato(String idTratta, int carrozza, int posto) {
        // Controlla biglietti
        String sqlBiglietti = """
        SELECT 1 FROM biglietti 
        WHERE id_tratta = ? AND carrozza = ? AND posto = ?
    """;

        try (PreparedStatement stmt = DBConnectionSingleton.getConnection().prepareStatement(sqlBiglietti)) {
            stmt.setString(1, idTratta);
            stmt.setInt(2, carrozza);
            stmt.setInt(3, posto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Controlla prenotazioni
        String sqlPrenotazioni = """
        SELECT 1 FROM prenotazioni 
        WHERE id_tratta = ? AND carrozza = ? AND postoPrenotazione = ?
    """;

        try (PreparedStatement stmt = DBConnectionSingleton.getConnection().prepareStatement(sqlPrenotazioni)) {
            stmt.setString(1, idTratta);
            stmt.setInt(2, carrozza);
            stmt.setInt(3, posto);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true se già presente
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public Prenotazione getPrenotazioneById(String id) {
        return prenotazioneRepo.getPrenotazionePerID(id);
    }
}
