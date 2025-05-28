package org.example.repository;


import org.example.model.Biglietto;
import org.example.utils.DatabaseManager;

import java.sql.*;
import java.util.*;

public class BigliettoRepository {

    public void aggiungiBiglietto(Biglietto b) {
        String sql = """
            INSERT INTO biglietti (id_Biglietto,classe, id_prenotazione, cf, id_tratta, posto, carrozza)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, b.getId_Biglietto());
            stmt.setString(2, b.getClasse());
            stmt.setString(3, b.getId_prenotazione());
            stmt.setString(4, b.getCF());
            stmt.setString(5, b.getId_tratta());
            stmt.setInt(6, b.getPosto());
            stmt.setInt(7, b.getCarrozza());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Biglietto> getBigliettiPerUtente(String cf) {
        List<Biglietto> lista = new ArrayList<>();
        String sql = "SELECT * FROM biglietti WHERE cf = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cf);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Biglietto b = new Biglietto();
                b.setId_Biglietto(rs.getString("id_Biglietto"));
                b.setClasse(rs.getString("classe"));
                b.setId_prenotazione(rs.getString("id_prenotazione"));
                b.setCF (rs.getString("cf"));
                b.setId_tratta(rs.getString("id_tratta"));
                b.setPosto(rs.getInt("posto"));
                b.setCarrozza(rs.getInt("carrozza"));


                lista.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
