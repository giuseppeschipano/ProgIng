package org.example.repository;


import org.example.model.Fedelta;
import org.example.model.Utente;
import org.example.utils.DatabaseManager;

import java.sql.*;
import java.util.*;

public class UtenteRepository {

    public void aggiungiUtente(Utente u) {
        String sql = "INSERT INTO utenti (cf, nomeUtente, cognomeUtente,  dataNascitaUtente, indirizzoUtente, id_fedelta) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getCFUtente());
            stmt.setString(2, u.getNomeUtente());
            stmt.setString(3, u.getCognomeUtente());
            stmt.setString(4, u.getDataNascitaUtente());
            stmt.setString(5, u.getIndirizzoUtente());
            stmt.setString(6, u.getCartaUtente() != null ? u.getCartaUtente().getID() : null);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Utente getUtenteByCF(String cf) {
        String sql = "SELECT * FROM utenti WHERE cf = ?";
        Utente u = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                u = new Utente();
                u.setCFUtente(rs.getString("cf"));
                u.setNomeUtente(rs.getString("nomeUtente"));
                u.setCognomeUtente(rs.getString("cognomeUtente"));
                u.setDataNascitaUtente(rs.getString("dataNascitaUtente"));
                u.setIndirizzoUtente(rs.getString("indirizzoUtente"));

                String idFedelta = rs.getString("id_fedelta");
                if (idFedelta != null ) {
                    Fedelta carta = new Fedelta();
                    carta.setID(idFedelta);
                 //   carta.ID = idFedelta;
                    u.setCartaUtente(carta);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return u;
    }
}
