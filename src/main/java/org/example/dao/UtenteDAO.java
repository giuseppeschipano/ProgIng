package org.example.dao;

import org.example.model.Fedelta;
import org.example.model.Utente;
import org.example.persistence.DBConnectionSingleton;

import java.sql.*;

public class UtenteDAO {

    public void aggiungiUtente(Utente u) {
        String sql = "INSERT INTO utenti (cf, nomeUtente, cognomeUtente, dataNascitaUtente, indirizzoUtente, cartaUtente, emailUtente, passwordUtente, is_admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = DBConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setString(1, u.getCFUtente());
            stmt.setString(2, u.getNomeUtente());
            stmt.setString(3, u.getCognomeUtente());
            stmt.setString(4, u.getDataNascitaUtente());
            stmt.setString(5, u.getIndirizzoUtente());
            stmt.setString(6, u.getCartaUtente() != null ? u.getCartaUtente().getID() : null);
            stmt.setString(7, u.getEmailUtente());
            stmt.setString(8, u.getPasswordUtente());
            stmt.setBoolean(9, u.isAdmin());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Utente getUtenteByCF(String cf) {
        String sql = "SELECT * FROM utenti WHERE cf = ?";
        Utente u = null;

        try (PreparedStatement stmt = DBConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setString(1, cf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                u = new Utente();
                u.setCFUtente(rs.getString("cf"));
                u.setNomeUtente(rs.getString("nomeUtente"));
                u.setCognomeUtente(rs.getString("cognomeUtente"));
                u.setDataNascitaUtente(rs.getString("dataNascitaUtente"));
                u.setIndirizzoUtente(rs.getString("indirizzoUtente"));
                u.setEmailUtente(rs.getString("emailUtente"));
                u.setPasswordUtente(rs.getString("passwordUtente"));
                u.setAdmin(rs.getBoolean("is_admin"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return u;
    }

    public Utente getUtenteByEmail(String email) {
        String sql = "SELECT * FROM utenti WHERE emailUtente = ?";
        try (PreparedStatement stmt = DBConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Utente u = new Utente();
                u.setCFUtente(rs.getString("cf"));
                u.setNomeUtente(rs.getString("nomeUtente"));
                u.setCognomeUtente(rs.getString("cognomeUtente"));
                u.setDataNascitaUtente(rs.getString("dataNascitaUtente"));
                u.setIndirizzoUtente(rs.getString("indirizzoUtente"));
                u.setEmailUtente(rs.getString("emailUtente"));
                u.setPasswordUtente(rs.getString("passwordUtente"));
                u.setAdmin(rs.getBoolean("is_admin"));

                Fedelta carta = new FedeltaDAO().getTesseraByCF(u.getCFUtente());
                u.setCartaUtente(carta);

                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateFedelta(String cf, String idFedelta, Connection conn) {
        String sql = "UPDATE utenti SET id_fedelta = ? WHERE cf = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idFedelta);
            stmt.setString(2, cf);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
