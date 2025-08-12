package org.example.dao;

import org.example.model.Biglietto;
import org.example.persistence.DBConnectionSingleton;
import java.sql.*;
import java.util.*;


public class BigliettoDAO {

    //Inserimento biglietto
    public void aggiungiBiglietto(Biglietto b) throws SQLException {
        String sql = """
            INSERT INTO biglietti (id_Biglietto,classe, id_prenotazione, cf, id_tratta, posto, carrozza)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        Connection conn = DBConnectionSingleton.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, b.getId_Biglietto());
            stmt.setString(2, b.getClasse());
            stmt.setString(3, b.getId_prenotazione());
            stmt.setString(4, b.getCF());
            stmt.setString(5, b.getId_tratta());
            stmt.setInt(6, b.getPosto());
            stmt.setInt(7, b.getCarrozza());
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
        }
    }


    //Metodo di ricerca di un biglietto (senza transazione)
    public Biglietto getBigliettoPerID (String id){
        String sql = "SELECT * FROM biglietti WHERE id_Biglietto = ?";
        try(PreparedStatement stmt = DBConnectionSingleton.getConnection().prepareStatement(sql)){
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                Biglietto b = new Biglietto();
                b.setId_Biglietto(rs.getString("id_biglietto"));
                b.setClasse(rs.getString("classe"));
                b.setId_prenotazione(rs.getString("id_prenotazione"));
                b.setCF (rs.getString("CF"));
                b.setId_tratta(rs.getString("id_tratta"));
                b.setPosto(rs.getInt("posto"));
                b.setCarrozza(rs.getInt("carrozza"));
                return b;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    public void aggiornaBiglietto(Biglietto b) {
        String sql = "UPDATE biglietti SET classe = ?, id_prenotazione = ?, cf = ?, id_tratta = ?, posto = ?, carrozza = ? WHERE id_Biglietto = ?";
        try(PreparedStatement stmt = DBConnectionSingleton.getConnection().prepareStatement(sql)){
            stmt.setString(1, b.getClasse());
            stmt.setString(2, b.getId_prenotazione());
            stmt.setString(3, b.getCF());
            stmt.setString(4, b.getId_tratta());
            stmt.setInt(5, b.getPosto());
            stmt.setInt(6, b.getCarrozza());
            stmt.setString(7, b.getId_Biglietto());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }


    //Recupera tutti i biglietti di un utente specifico
    public List<Biglietto> getBigliettiPerUtente(String cf) {
        List<Biglietto> lista = new ArrayList<>();
        String sql = "SELECT * FROM biglietti WHERE cf = ?";
        try (PreparedStatement stmt = DBConnectionSingleton.getConnection().prepareStatement(sql)) {
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

    //Elimina tutti i biglietti associati a una prenotazione specifica.
    public void eliminaBigliettiByPrenotazione(String idPrenotazione) throws SQLException {
        String sql = "DELETE FROM biglietti WHERE id_prenotazione = ?";
        Connection conn = DBConnectionSingleton.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idPrenotazione);
            stmt.executeUpdate();
        }
    }
}


