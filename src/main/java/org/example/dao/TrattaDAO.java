package org.example.dao;


import org.example.model.Tratta;
import org.example.persistence.DBConnectionSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrattaDAO {

    public void aggiungiTratta(Tratta t) {
        String sql = "INSERT INTO tratta (id_tratta, id_treno,oraPartenza, oraArrivo, stazionePartenza, stazioneArrivo, classiDisponibili,numeroPostiDisponibili,prezzo, data) VALUES (?, ?, ?,?,?,?,?,?,?, ?)";

        try (PreparedStatement stmt = DBConnectionSingleton.getConnection().prepareStatement(sql)){
            stmt.setString(1, t.getId_tratta());
            stmt.setString(2, t.getId_treno());
            stmt.setString(3, t.getOraPartenza());
            stmt.setString(4, t.getOraArrivo());
            stmt.setString(5, t.getStazionePartenza());
            stmt.setString(6, t.getStazioneArrivo());
            stmt.setString(7, t.getClassiDisponibili());
            stmt.setInt(8, t.getNumeroPostiDisponibili());
            stmt.setDouble(9, t.getPrezzo());
            stmt.setString(10, t.getData());


            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Tratta> getAllTratte() {
        List<Tratta> tratte = new ArrayList<>();
        String sql = "SELECT * FROM tratta";

        try (Connection conn = DBConnectionSingleton.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Tratta t = new Tratta();
                t.setId_tratta(rs.getString("id_tratta"));
                t.setId_treno(rs.getString("id_treno"));
                t.setOraPartenza(rs.getString("oraPartenza"));
                t.setOraArrivo(rs.getString("oraArrivo"));
                t.setStazionePartenza(rs.getString("stazionePartenza"));
                t.setStazioneArrivo(rs.getString("stazioneArrivo"));
                t.setClassiDisponibili(rs.getString("classiDisponibili"));
                t.setNumeroPostiDisponibili(rs.getInt("numeroPostiDisponibili"));
                t.setPrezzo(rs.getDouble("prezzo"));
                t.setData(rs.getString("data"));
                tratte.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tratte;
    }


    public List<Tratta> cercaTratta(String partenza, String arrivo, String data, String tipoTreno) {
        List<Tratta> tratte = new ArrayList<>();
        String sql = "SELECT * FROM tratta ta " +
                 "JOIN treni tr ON ta.id_treno = tr.id_Treno " +
                "WHERE ta.stazionePartenza = ? AND ta.stazioneArrivo = ? AND ta.data = ?";

        if (tipoTreno !=  null && !tipoTreno.isEmpty()){
            sql += " AND tr.tipologia = ?";
        }

        try (PreparedStatement stmt = DBConnectionSingleton.getConnection().prepareStatement(sql)){
            stmt.setString(1,partenza);
            stmt.setString(2, arrivo);
     //     stmt.setDate(3, Date.valueOf(data));
            stmt.setString(3, data);
            if (tipoTreno != null && !tipoTreno.isEmpty()){
                stmt.setString(4, tipoTreno);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Tratta t = new Tratta();
                t.setId_tratta(rs.getString("id_tratta"));
                t.setId_treno(rs.getString("id_treno"));
                t.setOraPartenza(rs.getString("oraPartenza"));
                t.setOraArrivo(rs.getString("oraArrivo"));
                t.setStazionePartenza(rs.getString("stazionePartenza"));
                t.setStazioneArrivo(rs.getString("stazioneArrivo"));
                t.setClassiDisponibili(rs.getString("classiDisponibili"));
                t.setNumeroPostiDisponibili(rs.getInt("numeroPostiDisponibili"));
                t.setPrezzo(rs.getDouble("prezzo"));
                t.setData(rs.getString("data"));
                tratte.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tratte;
    }



    public Tratta getTrattaById(String id,Connection conn) {
        String sql = "SELECT * FROM tratta WHERE id_tratta = ?";
        Tratta t = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String id_Tratta = rs.getString("id_tratta");
                String id_Treno = rs.getString("id_treno");
                String oraPartenza = rs.getString("oraPartenza");
                String oraArrivo = rs.getString("oraArrivo");
                String stazionePartenza = rs.getString("stazionePartenza");
                String stazioneArrivo = rs.getString("stazioneArrivo");
                String classiDisponibili = rs.getString("classiDisponibili");
                int numeroPostiDisponibili = rs.getInt("numeroPostiDisponibili");
                double prezzo = rs.getDouble("prezzo");
                String data = rs.getString("data");

                t = new Tratta();
                t.setId_tratta(id_Tratta);
                t.setId_treno(id_Treno);
                t.setOraPartenza(oraPartenza);
                t.setOraArrivo(oraArrivo);
                t.setStazionePartenza(stazionePartenza);
                t.setStazioneArrivo(stazioneArrivo);
                t.setClassiDisponibili(classiDisponibili);
                t.setNumeroPostiDisponibili(numeroPostiDisponibili);
                t.setPrezzo(prezzo);
                t.setData(data);
                return t;

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updatePostiDisponibili(String idTratta, int nuoviPosti,Connection conn) {
        String sql = "UPDATE tratta SET numeroPostiDisponibili = ? WHERE id_tratta = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nuoviPosti);
            stmt.setString(2, idTratta);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPostiDisponibili(Connection conn, String idTratta) throws SQLException {
        String sql = "SELECT numeroPostiDisponibili FROM tratta WHERE id_tratta = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idTratta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("numeroPostiDisponibili");
            } else {
                throw new SQLException("Tratta non trovata: " + idTratta);
            }
        }
    }


    public void decrementaPostiDisponibili(Connection conn, String idTratta, int quanti) throws SQLException {
        Tratta t = getTrattaById(idTratta, conn);
        if (t != null){
            updatePostiDisponibili(idTratta, t.getNumeroPostiDisponibili() - quanti, conn);
        }
    }

    public void incrementaPostiDisponibili(Connection conn, String idTratta, int quanti) throws SQLException {
        Tratta t = getTrattaById(idTratta, conn);
        if (t != null){
            updatePostiDisponibili(idTratta, t.getNumeroPostiDisponibili() + quanti, conn);
        }
    }

    //Senza Transazione
    public Tratta getTrattaByID(String idTratta){
        try{
            return  getTrattaById(idTratta, DBConnectionSingleton.getConnection());
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
