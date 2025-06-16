package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Promozione {
    String codicePromo, tipoTreno, inizioPromo, finePromo;
    int percentualeSconto;
    boolean soloFedelta;
}
