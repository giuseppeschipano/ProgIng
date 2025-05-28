package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Treno {
    String id_Treno, tipologia, stato;
    int carrozza;
    private List<Biglietto> bigliettiVenduti = new ArrayList<>(); //tengo traccia dei biglietti venduti per ogni treno

    /*
    public void aggiungiBiglietto(Biglietto b) {
        bigliettiVenduti.add(b);
    }
     */
}
