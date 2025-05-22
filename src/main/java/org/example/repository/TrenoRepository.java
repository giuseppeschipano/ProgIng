package org.example.repository;

import org.example.model.Treno;
import org.example.utils.DatabaseManager;

import java.sql.*;
import java.util.*;

public class TrenoRepository {

    public void aggiungiTreno(Treno t) {
        String sql = "INSERT INTO treni (id_treno, carrozza, posto, classi, tipologia) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, t.getIDTreno());
            stmt.setString(2, t.getCarrozza());
            stmt.setString(3, t.getPosto());
            stmt.setString(4, t.getClassi());
            stmt.setString(5, t.getTipologia());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Treno getTrenoById(String id) {
        String sql = "SELECT * FROM treni WHERE id_treno = ?";
        Treno t = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                t = new Treno();
                t.setIDTreno(rs.getString("id_treno"));
                t.setCarrozza(rs.getString("carrozza"));
                t.setPosto(rs.getString("posto"));
                t.setClassi(rs.getString("classi"));
                t.setTipologia(rs.getString("tipologia"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return t;
    }

    public List<Treno> getAllTreni() {
        List<Treno> lista = new ArrayList<>();
        String sql = "SELECT * FROM treni";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Treno t = new Treno();
                t.setIDTreno(rs.getString("id_treno"));
                t.setCarrozza(rs.getString("carrozza"));
                t.setPosto(rs.getString("posto"));
                t.setClassi(rs.getString("classi"));
                t.setTipologia(rs.getString("tipologia"));
                lista.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}

