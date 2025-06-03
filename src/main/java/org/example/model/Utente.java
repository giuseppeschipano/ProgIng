package org.example.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Utente {
    String nomeUtente, cognomeUtente, CFUtente, dataNascitaUtente, indirizzoUtente;
    Fedelta cartaUtente;
    String emailUtente, passwordUtente;
}
