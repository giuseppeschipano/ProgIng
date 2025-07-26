package org.example.service;

import org.example.dao.FedeltaDAO;
import org.example.model.Fedelta;
import org.example.model.Promozione;
import org.example.persistence.DBConnectionSingleton;
import org.example.dao.PromozioneDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PromozioneService {
    private final PromozioneDAO promoDataBase;
    private final FedeltaService serviceFedelta;
    private  final FedeltaDAO fedeltaDAO = new FedeltaDAO();

    public PromozioneService() {
        this.promoDataBase = new PromozioneDAO();
        this.serviceFedelta = new FedeltaService();
    }

    public void addNewPromotion(Promozione promo) throws SQLException {
        promoDataBase.addPromozione(promo);
    }

    public List<Promozione> promoSoloFedelta(String CF) {
        boolean check = serviceFedelta.hasTessera(CF);
        return promoDataBase.promozioniAttive(null, check, null);
    }

    // si intende valida per tutti
    public List<Promozione> promoSuTrenoSpecifico(String idtreno) {
        return promoDataBase.promozioniAttive(idtreno, false, null);
    }

    // si intende valida per tutti
    public List<Promozione> promoDataSpecifica(String data) {
        return promoDataBase.promozioniAttive(null, false, data);
    }

    public List<Promozione> getPromozioniPerUtente(String cf) {
        List<Promozione> tutte = promoDataBase.getAllPromozioni();
        List<Promozione> valide = new ArrayList<>();
        Fedelta tessera = fedeltaDAO.getTesseraByCF(cf);
        boolean haFedelta = (tessera != null);

        LocalDateTime now = LocalDateTime.now();

        for (Promozione p : tutte) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime inizio = LocalDateTime.parse(p.getInizioPromo(), formatter);
            LocalDateTime fine = LocalDateTime.parse(p.getFinePromo(), formatter);


            boolean inPeriodo = now.isAfter(inizio) && now.isBefore(fine);
            boolean promoValida = inPeriodo && (!p.isSoloFedelta() || haFedelta);

            if (promoValida) {
                valide.add(p);
            }
        }
        return valide;
    }
}