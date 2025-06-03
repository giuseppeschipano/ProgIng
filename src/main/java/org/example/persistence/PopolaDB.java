package org.example.persistence;

import org.example.dao.TrenoDAO;
import org.example.model.Treno;

public class PopolaDB {

    public static void main(String[] args){
        InitDB.init();

        TrenoDAO trenoDB = new TrenoDAO();
        Treno treno = new Treno();

        treno.setId_Treno("EC-7893");
        treno.setTipologia("ECONOMY");
        treno.setStato("In transito");
        trenoDB.aggiungiTreno(treno);

    }
}
