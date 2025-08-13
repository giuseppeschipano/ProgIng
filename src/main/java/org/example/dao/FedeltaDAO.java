package org.example.dao;

import org.example.model.Fedelta;
import org.example.persistence.DBConnectionSingleton;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class FedeltaDAO {

    public void aggiungiTessera(Fedelta f) {
        String sql = "INSERT INTO fedelta (id, CFPossessoreTessera,puntiFedelta) VALUES (?, ?, ?)";

        try(PreparedStatement stmt = DBConnectionSingleton.getConnection().prepareStatement(sql)){
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
        try(PreparedStatement stmt = DBConnectionSingleton.getConnection().prepareStatement(sql)){
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

    public void incrementaPunti(String cf, int puntiDaAggiungere){
        String sql = "UPDATE fedelta SET puntiFedelta = puntiFedelta + ? WHERE CFPossessoreTessera = ?";
        try(PreparedStatement stmt = DBConnectionSingleton.getConnection().prepareStatement(sql)){
            stmt.setInt(1, puntiDaAggiungere);
            stmt.setString(2, cf);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                System.out.println("Nessuna tessera fedeltà trovata per " + cf);
            } else {
                System.out.println("Punti fedeltà aggiornati per " + cf);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
