package org.example.service;

import org.example.dao.FedeltaDAO;
import org.example.dao.UtenteDAO;
import org.example.model.Fedelta;
import org.example.persistence.DBConnectionSingleton;
import java.sql.Connection;
import java.sql.SQLException;

public class FedeltaService {

    private final FedeltaDAO fedeltaDAO;
    private  final UtenteDAO utenteDAO;

    public FedeltaService() {
        this.fedeltaDAO = new FedeltaDAO();
        this.utenteDAO = new UtenteDAO();
    }


    public Fedelta getTesseraByCF(String cf) {
        return fedeltaDAO.getTesseraByCF(cf);
    }


    public boolean hasTessera(String cf) {
        Fedelta tessera = fedeltaDAO.getTesseraByCF(cf);
        return tessera != null;
    }


    public void aggiungiTessera(Fedelta f) throws SQLException {
        Connection conn = null;
      try {
          conn = DBConnectionSingleton.getConnection();
          conn.setAutoCommit(false);
          fedeltaDAO.aggiungiTessera(f);
          utenteDAO.updateFedelta(f.getCFPossessoreTessera(), f.getID());
          conn.commit();
      } catch ( SQLException e){
          if (conn != null) {
              conn.rollback();
          }
          e.printStackTrace();
      } finally {
          if (conn != null) {
              conn.setAutoCommit(true);
          }
      }
    }


    public void incrementaPunti(String cf, int punti) {
        Connection conn = null;
        try {
            conn = DBConnectionSingleton.getConnection();
            conn.setAutoCommit(false);
            fedeltaDAO.incrementaPunti(cf, punti);
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}