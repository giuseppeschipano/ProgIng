package org.example.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class InitDB {
    public static void init() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS utenti (
                     cf VARCHAR PRIMARY KEY,
                    nomeUtente VARCHAR(100),
                    cognomeUtente VARCHAR(100),
                    dataNascitaUtente VARCHAR(100),
                    indirizzoUtente VARCHAR(100),
                    id_fedelta VARCHAR(100),
                    
                    FOREIGN KEY (id_fedelta) REFERENCES fedelta(id)
                );  
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS fedelta(
                    id VARCHAR PRIMARY KEY,
                    CFPossessoreTessera VARCHAR (100),
                    puntiFedelta INT,
                                                  
                    FOREIGN KEY (CFPossessoreTessera) REFERENCES utenti(cf)                               
                )
            """);

            // Quando poi andremo a lavorare sulle prenotazioni/acquisto biglietto
            // Mi devo ricordare che qualunque UPDATE venga fatto su questa tabella
            // Per quanto riguarda numPostiDisponibili, oraPartenza/oraArrivo (In caso di ritardi ecc. ecc.)
            // Vanno tutte fatte in TRANSAZIONE quindi accertarsi che l'operazione risulti atomica
            // e che rispetto le proprietà ACID

            //classiDisponibili è una stringa formattata come segue:
            // "economy business luxury"
            // quindi simula una lista, quando devo fare il parsing uso StringTokenizer con divisore di base


            //oraPartenza, oraArrivo definite secondo pattern dd/MM/yyyy HH:mm:ss es 13/08/2024 13:30:00
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
                    
                    FOREIGN KEY (id_treno) REFERENCES treni(id_Treno)
                ) 
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS treni (
                    id_treno VARCHAR(20) PRIMARY KEY,
                    tipologia VARCHAR(100),
                    stato VARCHAR(100),
                    carrozza INT
                );
            """);

            // Logica di business: ogni tot si controllano tutte le prenotazioni
            // Le prenotazioni a cui manca un giorno per la scadenza implicano una notifica al cliente
            // che ricorda di acquistare il biglietto
            // Le prenotazioni scadute vengono eliminate dal DB e il cliente viene notificato
            // dell'operazione
            // Data scadenza inoltre è uguale a idViaggio(oraPartenza) -1 giorno
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


            // logica di business gestisce il calcolo dei posti disponibili per carrozza:
            // per semplicità si presuppone che ogni carrozza abbia 100 posti disponibili
            // quindi
            // 0. L'utente vuole sapere quali posti sono disponibili per una determinata carrozza in un determinato treno
            // 1. Implemento metodo che mi permette di fare seguente query:
            /**
             * SELECT p.posto
             * FROM prenotazioni p
             * JOIN viaggi v ON p.idViaggio = v.id
             * WHERE v.idTreno = ? (parametro inserito dal cliente) AND p.numCarrozza = ? (parametro inserito dal cliente)
             */
            // Questo metodo ritorna una lista di int che rappresentano i posti prenotati in quella carrozza
            // 2. Implemento un metodo che mi permette di fare seguente query:
            /**
             * SELECT b.posto
             * FROM biglietti b
             * JOIN viaggi v ON b.idViaggio = v.id
             * WHERE v.idTreno = ? AND b.numCarrozza = ?
             */
            // Che ritornerà una lista come il metodo precedente
            // 3. Creo una lista di tutti i posti occupati come somma delle due liste create in precedenza
            // 4. Sottraggo questa lista alla lista di tutti i posti della carrozza
            // 5. ritorno la lista ottenuta dalla sottrazione che rappresenta i posti disponibili


            // ricorda di implementare check all'interno di logica di business che si assicuri che
            // una prenotazione/acquisto biglietto NON possa essere effettuato
            // se si cerca di prenotare/acquistare posto già occupato.
            // tutta questa operazione di acquisto deve essere TRANSAZIONALE
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS biglietti (
                    id_Biglietto VARCHAR PRIMARY KEY,
                    classe VARCHAR(100),
                    id_prenotazione VARCHAR(100),
                    cf VARCHAR(100),
                    id_tratta VARCHAR(100),
                    posto INT,
                    carrozza INT,
                    
                    FOREIGN KEY (cf) REFERENCES utenti(cf),
                    FOREIGN KEY (id_tratta) REFERENCES tratta(id_tratta),
                    FOREIGN KEY (id_prenotazione) REFERENCES prenotazioni(id_Prenotazione)
                );
            """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}