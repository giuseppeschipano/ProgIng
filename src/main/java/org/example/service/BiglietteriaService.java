package org.example.service;

import org.example.dao.TrattaDAO;
import org.example.model.Biglietto;
import org.example.dao.BigliettoDAO;
import org.example.model.Tratta;
import org.example.persistence.DBConnectionSingleton;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BiglietteriaService {

    private final BigliettoDAO bigliettoRepo;
    private final TrattaService trattaService;
    private final TrattaDAO trattaDAO = new TrattaDAO();

    public BiglietteriaService(BigliettoDAO brepo, TrattaService trattaService) {
        this.bigliettoRepo = brepo;
        this.trattaService = trattaService;
    }

    public boolean acquistaBiglietti(List<Biglietto> biglietti) {
        if (biglietti == null || biglietti.isEmpty()) return false;
        String idTratta = biglietti.get(0).getId_tratta();  // Si assume che tutti i biglietti siano per la stessa tratta
        int numeroBiglietti = biglietti.size();

        try (Connection conn = DBConnectionSingleton.getConnection()) {
            conn.setAutoCommit(false);

            int disponibili = trattaService.getPostiDisponibili(idTratta);
            if (disponibili < numeroBiglietti) {
                System.out.println("Posti insufficienti per la tratta " + idTratta);
                conn.rollback();
                return false;
            }
            for (Biglietto b : biglietti) {
                bigliettoRepo.aggiungiBiglietto(b);
                System.out.println("Biglietto acquistato per: " + b.getCF());
            }

            trattaService.decrementaPostiDisponibili(idTratta, numeroBiglietti);
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try{
                DBConnectionSingleton.getConnection().rollback();
            } catch(SQLException ex){
                ex.printStackTrace();
            }
            return false;
        } finally {
            try{
                DBConnectionSingleton.getConnection().setAutoCommit(true);
            } catch (SQLException ignore) {}
        }
    }

    public static boolean isValidCardNumber(String numeroCarta) {
        if (numeroCarta == null || numeroCarta.trim().isEmpty()) return false;
        int sum = 0;
        boolean alternate = false;
        for (int i = numeroCarta.length() - 1; i >= 0; i--) {
            char c = numeroCarta.charAt(i);
            if (!Character.isDigit(c)) return false;

            int n = c - '0';
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }

    public boolean aggiornaBiglietto(String idBiglietto, String nuovaTrattaId, String nuovaClasse) {
        try (Connection conn = DBConnectionSingleton.getConnection()) {
            conn.setAutoCommit(false);

            // Recupero biglietto
            Biglietto biglietto = bigliettoRepo.getBigliettoPerID(idBiglietto);
            if (biglietto == null) {
                conn.rollback();
                return false;
            }

            Tratta trattaOriginale = trattaDAO.getTrattaByID(biglietto.getId_tratta());
            Tratta nuovaTratta = trattaDAO.getTrattaByID(nuovaTrattaId);
            if (nuovaTratta == null) {
                conn.rollback();
                return false;
            }

            // Aggiornamento posti: +1 alla vecchia tratta, -1 alla nuova
            trattaOriginale.setNumeroPostiDisponibili(trattaOriginale.getNumeroPostiDisponibili() + 1);
            nuovaTratta.setNumeroPostiDisponibili(nuovaTratta.getNumeroPostiDisponibili() - 1);
            trattaDAO.updateTratta(trattaOriginale);
            trattaDAO.updateTratta(nuovaTratta);

            // Aggiornamento biglietto
            biglietto.setId_tratta(nuovaTrattaId);
            biglietto.setClasse(nuovaClasse);
            bigliettoRepo.aggiornaBiglietto(biglietto);
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

