package org.example.model;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import  lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class Biglietto {
    String stazioneDiPartenza, stazioneDiArrivo, classe, treno, posto,  numeroPrenotazione;
    String nomeUtente, cognomeUtente, CF;
    String dataPartenzaTreno, dataArrivoTreno;
    int carrozza, codiceTreno, IDfedelta;

}
