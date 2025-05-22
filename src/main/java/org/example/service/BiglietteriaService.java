package org.example.service;


import org.example.model.Biglietto;
import org.example.repository.BigliettoRepository;

public class BiglietteriaService {

    private final BigliettoRepository bigliettoRepo;

    public BiglietteriaService(BigliettoRepository repo) {
        this.bigliettoRepo = repo;
    }

    public void acquistaBiglietto(Biglietto b) {
        // Eventuale logica extra: validazioni, sconti, ecc.
        bigliettoRepo.aggiungiBiglietto(b);
        System.out.println("Biglietto acquistato per: " + b.getNomeUtente());
    }
}

