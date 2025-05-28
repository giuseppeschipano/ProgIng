package org.example.repository;


import org.example.model.Tratta;
import org.example.utils.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TrattaRepository {

    public void aggiungiTratta(Tratta t) {
        String sql = "INSERT INTO tratta (id_tratta, id_treno,oraPartenza, oraArrivo, stazionePartenza, stazioneArrivo, classiDisponibili,numeroPostiDisponibili,prezzo) VALUES (?, ?, ?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getId_tratta());
            stmt.setString(2, t.getId_treno());
            stmt.setString(3, t.getOraPartenza());
            stmt.setString(4, t.getOraArrivo());
            stmt.setString(5, t.getStazionePartenza());
            stmt.setString(6, t.getStazioneArrivo());
            stmt.setString(7, t.getClassiDisponibili());
            stmt.setInt(8, t.getNumeroPostiDisponibili());
            stmt.setDouble(9, t.getPrezzo());


            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
