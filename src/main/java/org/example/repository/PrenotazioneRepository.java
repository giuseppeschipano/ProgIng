package org.example.repository;


import org.example.model.Prenotazione;
import org.example.utils.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneRepository {

    public void aggiungiPrenotazione(Prenotazione p) {
        String sql = """
            INSERT INTO prenotazioni (id_Prenotazione, dataScadenza, CFUtente,id_tratta,postoPrenotazione, carrozza)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getId_Prenotazione());
            stmt.setString(2, p.getDataScadenza());
            stmt.setString(3, p.getCFUtente());
            stmt.setString(4, p.getId_tratta());
            stmt.setInt(5, p.getPostoPrenotazione());
            stmt.setInt(6, p.getCarrozza());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Prenotazione> getPrenotazionePerUtente(String cf) {
        List<Prenotazione> lista = new ArrayList<>();
        String sql = "SELECT * FROM prenotazioni WHERE CFUtente = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cf);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Prenotazione p = new Prenotazione();
                p.setId_Prenotazione(rs.getString("id_Prenotazione"));
                p.setDataScadenza(rs.getString("dataScadenza"));
                p.setCFUtente(rs.getString("CFUtente"));
                p.setId_tratta(rs.getString("id_tratta"));
                p.setPostoPrenotazione (rs.getInt("postoPrenotazione")); ;
                p.setCarrozza(rs.getInt("carrozza"));



                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
