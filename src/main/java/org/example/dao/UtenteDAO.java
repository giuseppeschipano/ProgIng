package org.example.dao;


import org.example.model.Fedelta;
import org.example.model.Utente;
import org.example.persistence.DBConnectionSingleton;

import java.sql.*;

public class UtenteDAO {

    public void aggiungiUtente(Utente u) {
        String sql = "INSERT INTO utenti (cf, nomeUtente, cognomeUtente,  dataNascitaUtente, indirizzoUtente, cartaUtente, emailUtente, passwordUtente) VALUES (?, ?, ?, ?, ?, ?, ? , ?)";

        try (PreparedStatement stmt = DBConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setString(1, u.getCFUtente());
            stmt.setString(2, u.getNomeUtente());
            stmt.setString(3, u.getCognomeUtente());
            stmt.setString(4, u.getDataNascitaUtente());
            stmt.setString(5, u.getIndirizzoUtente());
            stmt.setString(6, String.valueOf(u.getCartaUtente()));
            stmt.setString(7, u.getEmailUtente());
            stmt.setString(8, u.getPasswordUtente());
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
                String codiceFiscale = rs.getString("cf");
                String nomeUtente = rs.getString("nomeUtente");
                String cognomeUtente = rs.getString("cognomeUtente");
                String dataNascitaUtente = rs.getString("dataNascitaUtente");
                String indirizzoUtente = rs.getString("indirizzoUtente");
                String emailUtente = rs.getString("emailUtente");
                String passwordUtente = rs.getString("passwordUtente");



                u = new Utente();
                Fedelta cartaUtente= new FedeltaDAO().getTesseraByCF(cf);

                u.setCFUtente(codiceFiscale);
                u.setNomeUtente(nomeUtente);
                u.setCognomeUtente(cognomeUtente);
                u.setDataNascitaUtente(dataNascitaUtente);
                u.setIndirizzoUtente(indirizzoUtente);
                u.setCartaUtente(cartaUtente);
                u.setEmailUtente(emailUtente);
                u.setPasswordUtente(passwordUtente);
                return u;


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
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
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void updateFedelta (String cf, boolean fedelta, Connection conn){
        String sql = "UPDATE utenti SET fedelta = ? WHERE cf = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setBoolean(1, fedelta);
            stmt.setString(2, cf);
            stmt.executeQuery(); //  stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
