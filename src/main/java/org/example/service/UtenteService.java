package org.example.service;

import org.example.dao.FedeltaDAO;
import org.example.dao.UtenteDAO;
import org.example.model.Fedelta;
import org.example.model.Utente;
import org.example.persistence.DBConnectionSingleton;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UtenteService {

    private final FedeltaService fedeltaService;
    private final UtenteDAO utenteDAO;
    private final FedeltaDAO fedeltaDAO;

    private Map<String, String> credenzialiAmministratori = new HashMap<>();

    public UtenteService() {
        this.fedeltaService = new FedeltaService();
        this.utenteDAO = new UtenteDAO();
        this.fedeltaDAO = new FedeltaDAO();
        credenzialiAmministratori.put("admin", "admin");
    }




    public void registraUtente(Utente utente) {
        Connection conn = null;
        try {
            conn = DBConnectionSingleton.getConnection();
            conn.setAutoCommit(false);

            utenteDAO.aggiungiUtente(utente); // puoi anche fare una variante con conn
            if (utente.getCartaUtente() != null) {
                fedeltaService.aggiungiTessera(utente.getCartaUtente()); // usa metodo transazionale
            }

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


    /*
    public void registraUtente(Utente utente) {
        utenteDAO.aggiungiUtente(utente);
        if (utente.getCartaUtente() != null) {
            fedeltaDAO.aggiungiTessera(utente.getCartaUtente());
        }
    }
     */
    public Utente getUtente(String cf) {
        Utente u = utenteDAO.getUtenteByCF(cf);
        Fedelta tessera = fedeltaDAO.getTesseraByCF(cf);
        u.setCartaUtente(tessera);
        return u;
    }

    public Utente login(String email, String password){
        Utente u = utenteDAO.getUtenteByEmail(email);
        if (u != null && u.getPasswordUtente().equals(password)) {
            System.out.println("Login riuscito per: " + u.getNomeUtente());
            return u;
        } else {
            System.out.println("Email o password errati.");
            return null;
        }
    }

    public Utente loginAdmin(String email, String password) {
        if (!credenzialiAmministratori.containsKey(email)) {
            return null;
        } else{
            return login(email, password);
        }
    }
}
