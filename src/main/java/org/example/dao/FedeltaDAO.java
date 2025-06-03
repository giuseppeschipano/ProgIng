package org.example.dao;


import org.example.model.Fedelta;
import org.example.persistence.DBConnectionSingleton;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class FedeltaDAO {

    public void aggiungiTessera(Fedelta f) {
        String sql = "INSERT INTO fedelta (id, CFPossessoreTessera,puntiFedelta) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = DBConnectionSingleton.getConnection().prepareStatement(sql)) {
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

        try (PreparedStatement stmt = DBConnectionSingleton.getConnection().prepareStatement(sql)){
            stmt.setString(1, cf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String codiceFiscale = rs.getString("CFPossessoreTessera");
                int punti = rs.getInt("puntiFedelta");

                f = new Fedelta();
                f.setID(id);
                f.setCFPossessoreTessera(codiceFiscale);
                f.setPunti(punti);
                return f;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
