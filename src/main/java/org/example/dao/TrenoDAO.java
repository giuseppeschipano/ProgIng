package org.example.dao;

import org.example.model.Treno;
import org.example.persistence.DBConnectionSingleton;

import java.sql.*;
import java.util.*;

public class TrenoDAO {

    public void aggiungiTreno(Treno t) {
        String sql = "INSERT INTO treni (id_Treno, tipologia, stato, carrozza) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnectionSingleton.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, t.getId_Treno());
            stmt.setString(2, t.getTipologia());
            stmt.setString(3, t.getStato());
            stmt.setInt(4, t.getCarrozza());


            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Treno getTrenoById(String id) {
        String sql = "SELECT * FROM treni WHERE id_treno = ?";
        Treno t = null;

        try (Connection conn = DBConnectionSingleton.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                t = new Treno();
                t.setId_Treno(rs.getString("id_treno"));
                t.setTipologia(rs.getString("tipologia"));
                t.setStato(rs.getString("stato"));
                t.setCarrozza(rs.getInt("carrozza"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return t;
    }

    public List<Treno> getAllTreni() {
        List<Treno> lista = new ArrayList<>();
        String sql = "SELECT * FROM treni";

        try (Connection conn = DBConnectionSingleton.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Treno t = new Treno();
                t.setId_Treno(rs.getString("id_treno"));
                t.setTipologia(rs.getString("tipologia"));
                t.setStato(rs.getString("stato"));
                t.setCarrozza(rs.getInt("carrozza"));
                lista.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}

