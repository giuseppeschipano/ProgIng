package org.example.ui;

import org.example.model.*;
import org.example.repository.*;
import org.example.service.BiglietteriaService;
import org.example.utils.InitDB;

public class ConsoleClient {
    /*
    public static void main(String[] args) {
        InitDB.init(); // crea le tabelle

        // Repository
        BigliettoRepository bigliettoRepo = new BigliettoRepository();
        PrenotazioneRepository prenotazioneRepo = new PrenotazioneRepository();
        UtenteRepository utenteRepo = new UtenteRepository();
        TrenoRepository trenoRepo = new TrenoRepository();
        TrattaRepository trattaRepo = new TrattaRepository();
        FedeltaRepository fedeltaRepo = new FedeltaRepository();

        // Esempio dati
        Fedelta f = new Fedelta("F123", "RSSMRA90A01H501Z", 30);
        fedeltaRepo.aggiungiTessera(f);

        Utente u = new Utente();
        u.setNomeUtente("Mario");
        u.setCognomeUtente("Rossi");
        u.setCFUtente("RSSMRA90A01H501Z");
        u.setDataNascitaUtente("01/01/1990");
        u.setIndirizzoUtente("Via Roma 1");
        u.setCartaUtente(f);
        u.setBigliettiUtente(bigliettoRepo);
        u.setPrenotazioniUtente(prenotazioneRepo);
        utenteRepo.aggiungiUtente(u);

        Treno t = new Treno("T123", "Frecciarossa", "Attivo", 5);
        trenoRepo.aggiungiTreno(t);

        Tratta tratta = new Tratta(100, "TR123", "12/06/2025 14:00:00", "12/06/2025 17:00:00",
                "Roma", "Milano", "economy business", 59.99);
        trattaRepo.aggiungiTratta(tratta);

        Biglietto b = new Biglietto("B001", "economy", null, u.getCFUtente(), 1, 12, 2);
        BiglietteriaService service = new BiglietteriaService(bigliettoRepo, trenoRepo);
        service.acquistaBiglietto(b);

        // Recupera biglietti e prenotazioni
        System.out.println("Biglietti acquistati da " + u.getNomeUtente() + ":");
        u.getBigliettiAcquistati().forEach(System.out::println);

        System.out.println("Prenotazioni effettuate da " + u.getNomeUtente() + ":");
        u.getPrenotazioniEffettuate().forEach(System.out::println);
    }

     */
}