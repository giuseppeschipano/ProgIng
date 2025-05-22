package org.example.repository;


import org.example.model.Biglietto;
import org.example.utils.DatabaseManager;

import java.sql.*;
import java.util.*;

public class BigliettoRepository {

    public void aggiungiBiglietto(Biglietto b) {
        String sql = """
            INSERT INTO biglietti (numero_prenotazione, stazione_partenza, stazione_arrivo, classe, treno, posto,
                                   data_partenza, data_arrivo, carrozza, nome, cognome, cf, id_fedelta)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, b.getNumeroPrenotazione());
            stmt.setString(2, b.getStazioneDiPartenza());
            stmt.setString(3, b.getStazioneDiArrivo());
            stmt.setString(4, b.getClasse());
            stmt.setString(5, b.getTreno());
            stmt.setString(6, b.getPosto());
            stmt.setString(7, b.getDataPartenzaTreno());
            stmt.setString(8, b.getDataArrivoTreno());
            stmt.setInt(9, b.getCarrozza());
            stmt.setString(10, b.getNomeUtente());
            stmt.setString(11, b.getCognomeUtente());
            stmt.setString(12, b.getCF());
            stmt.setInt(13, b.getIDfedelta());
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
                b.setNumeroPrenotazione(rs.getString("numero_prenotazione"));
                b.setStazioneDiPartenza(rs.getString("stazione_partenza"));
                b.setStazioneDiArrivo(rs.getString("stazione_arrivo"));
                b.setClasse(rs.getString("classe"));
                b.setTreno (rs.getString("treno")) ;
                b.setPosto(rs.getString("posto"));
                b.setDataPartenzaTreno(rs.getString("data_partenza"));
                b.setDataArrivoTreno(rs.getString("data_arrivo"));
                b.setCarrozza(rs.getInt("carrozza"));
                b.setNomeUtente(rs.getString("nome"));
                b.setCognomeUtente(rs.getString("cognome"));
                b.setCF(rs.getString("cf"));
                b.setIDfedelta(rs.getInt("id_fedelta"));

                lista.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
