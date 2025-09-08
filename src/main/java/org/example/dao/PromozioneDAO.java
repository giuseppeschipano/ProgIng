package org.example.dao;

import org.example.model.Promozione;
import org.example.persistence.DBConnectionSingleton;
import  java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PromozioneDAO {

    public void addPromozione(Promozione p){
        String sql = "INSERT INTO promozioni (codicePromo,  percentualeSconto, tipoTreno, inizioPromo, finePromo, soloFedelta ) VALUES (?, ?, ? ,?, ?, ?)";
        try (PreparedStatement stmt = DBConnectionSingleton.getConnection().prepareStatement(sql)){
            stmt.setString(1, p.getCodicePromo());
            stmt.setInt(2,p.getPercentualeSconto());
            stmt.setString(3,p.getTipoTreno());
            stmt.setString(4, p.getInizioPromo());
            stmt.setString(5, p.getFinePromo());
            stmt.setBoolean(6,p.isSoloFedelta());
            stmt.executeUpdate();
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    }



    public List<Promozione> getAllPromozioni() {
        List<Promozione> lista = new ArrayList<>();
        String sql = "SELECT * FROM promozioni";
        try (Connection conn = DBConnectionSingleton.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Promozione p = new Promozione();
                p.setCodicePromo(rs.getString("codicePromo"));
                p.setPercentualeSconto(rs.getInt("percentualeSconto"));
                p.setTipoTreno(rs.getString("tipoTreno"));
                p.setInizioPromo(rs.getString("inizioPromo"));
                p.setFinePromo(rs.getString("finePromo"));
                p.setSoloFedelta(rs.getBoolean("soloFedelta"));

                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
