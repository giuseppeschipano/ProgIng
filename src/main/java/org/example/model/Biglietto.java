package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import  lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Biglietto {
    String  id_Biglietto,classe, id_prenotazione,id_tratta;
    String  CF;
    int  posto, carrozza;
}
