package org.example.service;

import org.example.dao.FedeltaDAO;
import org.example.model.Fedelta;
import org.example.model.Promozione;
import org.example.dao.PromozioneDAO;
import java.time.LocalDate;
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

    public List<Promozione> getPromozioniPerUtente(String cf) {
        List<Promozione> tutte = promoDataBase.getAllPromozioni();
        List<Promozione> valide = new ArrayList<>();
        Fedelta tessera = fedeltaDAO.getTesseraByCF(cf);
        boolean haFedelta = (tessera != null);
        LocalDate now = LocalDate.now();
        for (Promozione p : tutte) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate inizio = LocalDate.parse(p.getInizioPromo(), formatter);
            LocalDate fine = LocalDate.parse(p.getFinePromo(), formatter);
            boolean inPeriodo = (now.isEqual(inizio) || now.isAfter(inizio)) && (now.isEqual(fine) || now.isBefore(fine));
            boolean promoValida = inPeriodo && (!p.isSoloFedelta() || haFedelta);
            if (promoValida) {
                valide.add(p);
            }
        }
        return valide;
    }
}