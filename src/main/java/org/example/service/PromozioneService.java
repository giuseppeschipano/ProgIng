package org.example.service;

import org.example.dao.FedeltaDAO;
import org.example.model.Fedelta;
import org.example.model.Promozione;
import org.example.dao.PromozioneDAO;
import java.sql.SQLException;
import java.time.LocalDate;
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
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  //    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataOra = now.format(formatter);
        return promoDataBase.promozioniAttive(null, check, dataOra);
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