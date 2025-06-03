package org.example.service;

import org.example.dao.FedeltaDAO;
import org.example.model.Fedelta;

public class FedeltaService {

    private final FedeltaDAO fedeltaDAO;

    public FedeltaService() {
        this.fedeltaDAO = new FedeltaDAO();
    }

    public Fedelta getTesseraByCF(String cf) {
        return fedeltaDAO.getTesseraByCF(cf);
    }

    public boolean isFedeltaUtente(String cf) {
        return fedeltaDAO.getTesseraByCF(cf) != null;
    }

    public int getPunti(String cf) {
        Fedelta tessera = fedeltaDAO.getTesseraByCF(cf);
        return tessera != null ? tessera.getPunti() : 0;
    }

    public void aggiungiTessera(Fedelta f) {
        fedeltaDAO.aggiungiTessera(f);
    }
}