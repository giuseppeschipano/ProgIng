package org.example.service;


import org.example.model.Biglietto;
import org.example.repository.BigliettoRepository;
import org.example.repository.TrenoRepository;

public class BiglietteriaService {

    private final BigliettoRepository bigliettoRepo;
    private final TrenoRepository trenoRepo;

    public BiglietteriaService(BigliettoRepository brepo, TrenoRepository trepo ) {
        this.bigliettoRepo = brepo;
        this.trenoRepo = trepo;
    }

    public void acquistaBiglietto(Biglietto b) {
        // Eventuale logica extra: validazioni, sconti, ecc.
        bigliettoRepo.aggiungiBiglietto(b);
        System.out.println("Biglietto acquistato per: " + b.getCF());
    }
}

