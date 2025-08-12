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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrenotazioneService {

    private final PrenotazioneDAO prenotazioneRepo;
    private final BigliettoDAO bigliettoRepo;
    private final TrattaDAO trattaRepo;

    public PrenotazioneService(PrenotazioneDAO prenotazioneRepo, BigliettoDAO bigliettoRepo, TrattaDAO trattaRepo) {
        this.prenotazioneRepo = prenotazioneRepo;
        this.bigliettoRepo = bigliettoRepo;
        this.trattaRepo = trattaRepo;
    }

    public void effettuaPrenotazione(Prenotazione p) throws SQLException {
        Connection conn = DBConnectionSingleton.getConnection();
        try {
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
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try{
            conn.setAutoCommit(true);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}

    public void confermaPrenotazione(Prenotazione p, Biglietto b) throws SQLException {
        Connection conn = DBConnectionSingleton.getConnection();
        try  {
            conn.setAutoCommit(false);
            bigliettoRepo.aggiungiBiglietto(b);
            bigliettoRepo.eliminaBigliettiByPrenotazione(p.getId_Prenotazione()); //per evitare violazioni di integrità referenziale quando voglio cancellare una prenotazione che ha dei biglietti ad essa collegati.(Eliminiamo prima i biglietti collegati, la cancellazione della prenotazione non causerà errori di foreign key)
            prenotazioneRepo.rimuoviPrenotazione(p.getId_Prenotazione());
            conn.commit();
            System.out.println("Prenotazione confermata e biglietto emesso.");
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("Errore durante la conferma prenotazione" );
            e.printStackTrace();
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public void controllaPrenotazioniScadute() {
        List<Prenotazione> tutte = prenotazioneRepo.getTuttePrenotazioni();
        LocalDate oraCorrente = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Prenotazione p : tutte) {
            try {
                LocalDate scadenza = LocalDate.parse(p.getDataScadenza(), formatter);
                if (oraCorrente.isAfter(scadenza)) {
                    annullaPrenotazione(p);
                }
            } catch (Exception e) {
                System.err.println("Errore nel parsing della data: " + p.getDataScadenza());
            }
        }
    }


    public void annullaPrenotazione(Prenotazione p) throws SQLException {
        Connection conn = DBConnectionSingleton.getConnection();
        try {
            conn.setAutoCommit(false);
            prenotazioneRepo.rimuoviPrenotazione(p.getId_Prenotazione());
            trattaRepo.incrementaPostiDisponibili(p.getId_tratta(), 1);
            conn.commit();
            System.out.println("Prenotazione " + p.getId_Prenotazione() + " annullata. Posto liberato.");
        } catch (SQLException e) {
            try{
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try{
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

    public int[] trovaPostoDisponibile(String idTratta) throws SQLException {
        final int POSTI_PER_CARROZZA = 70;
        Connection conn = DBConnectionSingleton.getConnection();
        String sqlCarrozze = """
        SELECT t.carrozza
        FROM tratta tr
        JOIN treni t ON tr.id_treno = t.id_treno
        WHERE tr.id_tratta = ?
        """;
        int numeroCarrozze = 0;
        try (PreparedStatement stmt = conn.prepareStatement(sqlCarrozze)) {
            stmt.setString(1, idTratta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    numeroCarrozze = rs.getInt("carrozza");
                }
            }
        }
        if (numeroCarrozze == 0) return null;
        Set<String> occupati = new HashSet<>();
        String sqlBiglietti = "SELECT carrozza, posto FROM biglietti WHERE id_tratta = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlBiglietti)) {
            stmt.setString(1, idTratta);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    occupati.add(rs.getInt("carrozza") + "-" + rs.getInt("posto"));
                }
            }
        }
        String sqlPrenotazioni = "SELECT carrozza, postoPrenotazione FROM prenotazioni WHERE id_tratta = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlPrenotazioni)) {
            stmt.setString(1, idTratta);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    occupati.add(rs.getInt("carrozza") + "-" + rs.getInt("postoPrenotazione"));
                }
            }
        }
        for (int c = 1; c <= numeroCarrozze; c++) {
            for (int p = 1; p <= POSTI_PER_CARROZZA; p++) {
                String key = c + "-" + p;
                if (!occupati.contains(key)) {
                    return new int[]{p, c};
                }
            }
        }
        return null;
    }
}
