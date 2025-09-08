package org.example.service;

import org.example.dao.FedeltaDAO;
import org.example.dao.UtenteDAO;
import org.example.model.Fedelta;
import org.example.model.Utente;

public class UtenteService {

    private final UtenteDAO utenteDAO;
    private final FedeltaDAO fedeltaDAO;


    public UtenteService() {
        this.utenteDAO = new UtenteDAO();
        this.fedeltaDAO = new FedeltaDAO();
    }


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
        Utente u = login(email, password);
        if (u != null && u.isAdmin()) {
            return u;
        } else {
            return null;
        }
    }
}
