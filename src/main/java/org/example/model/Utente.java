package org.example.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Utente {
    String nomeUtente, cognomeUtente, CFUtente, dataNascitaUtente, indirizzoUtente;
    Fedelta cartaUtente;
    String emailUtente, passwordUtente;
    boolean isAdmin;

}

