package org.example.service;

import org.example.dao.TrenoDAO;
import org.example.model.Treno;
import java.util.List;

public class TrenoService {

    private final TrenoDAO trenoDAO;

    public TrenoService() {
        this.trenoDAO = new TrenoDAO();
    }

    public void aggiungiTreno(Treno treno) {
        trenoDAO.aggiungiTreno(treno);
    }

    public Treno getTrenoById(String idTreno) {
        return trenoDAO.getTrenoById(idTreno);
    }

    public List<Treno> getTuttiTreni() {
        return trenoDAO.getAllTreni();
    }
}

