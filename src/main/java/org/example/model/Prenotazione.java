package org.example.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prenotazione {
    String id_Prenotazione, dataScadenza, CFUtente,id_tratta;
    int postoPrenotazione, carrozza;
}
