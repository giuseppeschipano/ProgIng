package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Tratta {
    int  numeroPostiDisponibili;
    String id_tratta, id_treno,oraPartenza, oraArrivo, stazionePartenza, stazioneArrivo, classiDisponibili;
    String data;
    double prezzo;
}
