package org.example.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.repository.BigliettoRepository;
import org.example.repository.PrenotazioneRepository;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Utente {
    String nomeUtente, cognomeUtente, CFUtente, dataNascitaUtente, indirizzoUtente;
    Fedelta cartaUtente;

    private BigliettoRepository bigliettiUtente;
    private PrenotazioneRepository prenotazioniUtente;

    //tengo traccia dei biglietti acquistati da un utente
    public List<Biglietto> getBigliettiAcquistati() {
        if (bigliettiUtente != null && CFUtente != null) {
            return bigliettiUtente.getBigliettiPerUtente(CFUtente);
        }
        return new ArrayList<>();
    }

     //tengo traccia delle prenotazioni effettuate da un utente
     public List<Prenotazione> getPrenotazioniEffettuate() {
         if (prenotazioniUtente != null && CFUtente != null) {
             return prenotazioniUtente.getPrenotazionePerUtente(CFUtente);
         }
         return new ArrayList<>();
     }

}
