package org.example.repository;


import org.example.model.Fedelta;
import org.example.utils.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class FedeltaRepository {

    public void aggiungiTessera(Fedelta f) {
        String sql = "INSERT INTO fedelta (id, CFPossessoreTessera,puntiFedelta) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, f.getID());
            stmt.setString(2, f.getCFPossessoreTessera());
            stmt.setInt(3, f.getPunti());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Fedelta getTesseraByCF(String cf){
        String sql = "SELECT * FROM fedelta WHERE CFPossessoreTessera = ?";
        Fedelta f = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, cf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                f = new Fedelta();
                f.setID(rs.getString("id"));
                f.setCFPossessoreTessera(rs.getString("CFPossessoreTessera"));
                f.setPunti(rs.getInt("puntiFedelta"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return f;
    }
}
