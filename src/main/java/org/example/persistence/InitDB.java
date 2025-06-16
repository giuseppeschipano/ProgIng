package org.example.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class InitDB {
    public static void init() {
        try (Connection conn = DBConnectionSingleton.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS utenti (
                     cf VARCHAR PRIMARY KEY,
                    nomeUtente VARCHAR(100),
                    cognomeUtente VARCHAR(100),
                    dataNascitaUtente VARCHAR(100),
                    indirizzoUtente VARCHAR(100),
                    cartaUtente VARCHAR (100),
                    emailUtente VARCHAR (100) UNIQUE,
                    passwordUtente VARCHAR (100),
                    id_fedelta VARCHAR,
                    
                   FOREIGN KEY (id_fedelta) REFERENCES fedelta(id)
                );  
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS treni (
                    id_treno VARCHAR(20) PRIMARY KEY,
                    tipologia VARCHAR(100),
                    stato VARCHAR(100),
                    carrozza INT
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS fedelta(
                    id VARCHAR(100) PRIMARY KEY,
                    CFPossessoreTessera VARCHAR (100),
                    puntiFedelta INT,
                                                  
                    FOREIGN KEY (CFPossessoreTessera) REFERENCES utenti(cf)                               
                )
            """);


            stmt.execute("""
                CREATE TABLE IF NOT EXISTS tratta (
                    id_tratta VARCHAR(100) PRIMARY KEY,
                    id_treno VARCHAR(100),
                    oraPartenza VARCHAR(100),
                    oraArrivo VARCHAR(100),
                    stazionePartenza VARCHAR(100),
                    stazioneArrivo VARCHAR(100),
                    classiDisponibili VARCHAR(100),
                    numeroPostiDisponibili INT,
                    prezzo DOUBLE,
                    data VARCHAR(100),
                    
                    FOREIGN KEY (id_treno) REFERENCES treni(id_treno)
                ) 
            """);



            stmt.execute("""
                CREATE TABLE IF NOT EXISTS prenotazioni (
                    id_Prenotazione VARCHAR(100) PRIMARY KEY,
                    dataScadenza VARCHAR(100),
                    CFUtente VARCHAR(100),
                    id_tratta VARCHAR(100),
                    postoPrenotazione INT,
                    carrozza INT,
                    
                    FOREIGN KEY (id_tratta) REFERENCES tratta(id_tratta),
                    FOREIGN KEY (CFUtente) REFERENCES utenti(cf)
                )
            """);



            stmt.execute("""
                CREATE TABLE IF NOT EXISTS biglietti (
                    id_Biglietto VARCHAR PRIMARY KEY,
                    classe VARCHAR(100),
                    id_prenotazione VARCHAR(100),
                    CF VARCHAR(100),
                    id_tratta VARCHAR(100),
                    posto INT,
                    carrozza INT,
                    
                    FOREIGN KEY (cf) REFERENCES utenti(cf),
                    FOREIGN KEY (id_tratta) REFERENCES tratta(id_tratta),
                    FOREIGN KEY (id_prenotazione) REFERENCES prenotazioni(id_Prenotazione)
                );
            """);


            stmt.execute("""
                 CREATE TABLE IF NOT EXISTS promozioni(
                     codicePromo  VARCHAR (100) PRIMARY KEY,
                     percentualeSconto INT,
                     tipoTreno  VARCHAR (100),
                     inizioPromo VARCHAR (100),
                     finePromo VARCHAR (100),
                     soloFedelta VARCHAR (100)
                     
                     )
                """ );


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
