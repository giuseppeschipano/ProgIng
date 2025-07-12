package org.example.service;

import org.example.model.Biglietto;
import org.example.dao.BigliettoDAO;
import org.example.dao.TrattaDAO;
import org.example.persistence.DBConnectionSingleton;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BiglietteriaService {

    private final BigliettoDAO bigliettoRepo;
    private final TrattaService trattaService;

    public BiglietteriaService(BigliettoDAO brepo, TrattaService trattaService) {
        this.bigliettoRepo = brepo;
        this.trattaService = trattaService;
    }

    public boolean acquistaBiglietti(List<Biglietto> biglietti) {
        if (biglietti == null || biglietti.isEmpty()) return false;

        String idTratta = biglietti.get(0).getId_tratta();  // Si assume che tutti i biglietti siano per la stessa tratta
        int numeroBiglietti = biglietti.size();

        try (Connection conn = DBConnectionSingleton.getConnection()) {
            conn.setAutoCommit(false); // Inizio transazione

            int disponibili = trattaService.getPostiDisponibili(idTratta);
            if (disponibili < numeroBiglietti) {
                System.out.println("Posti insufficienti per la tratta " + idTratta);
                conn.rollback();
                return false;
            }

            for (Biglietto b : biglietti) {
                bigliettoRepo.aggiungiBiglietto(b,conn);
                System.out.println("Biglietto acquistato per: " + b.getCF());
            }

            // Decrementa i posti una sola volta dopo aver aggiunto tutti i biglietti
            trattaService.decrementaPostiDisponibili(idTratta, numeroBiglietti);

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isValidCardNumber(String numeroCarta) {
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
}

