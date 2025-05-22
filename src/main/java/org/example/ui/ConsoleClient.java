package org.example.ui;

import org.example.model.Biglietto;
import org.example.repository.BigliettoRepository;
import org.example.service.BiglietteriaService;

public class ConsoleClient {
    public static void main(String[] args) {
        // Inizializzazione
        BigliettoRepository bigliettoRepo = new BigliettoRepository();
        BiglietteriaService servizio = new BiglietteriaService(bigliettoRepo);

        // Creazione biglietto
        Biglietto b = new Biglietto();
        b.setNumeroPrenotazione("B123");
        b.setStazioneDiPartenza("Roma");
        b.setStazioneDiArrivo("Milano");
        b.setClasse("1A");
        b.setTreno("IC234");
        b.setPosto("15B");
        b.setDataPartenzaTreno("2025-06-01");
        b.setDataArrivoTreno("2025-06-01");
        b.setCarrozza(2);
        b.setNomeUtente("Mario");
        b.setCognomeUtente("Rossi");
        b.setCF("RSSMRA80A01H501U");
        b.setIDfedelta(1);

        // Acquisto
        servizio.acquistaBiglietto(b);
    }
}
