package org.example.persistence;

import org.example.dao.*;
import org.example.model.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PopolaDB {

    public static void popola() throws SQLException {
        InitDB.init();
        try (Connection connection = DBConnectionSingleton.getConnection();
             Statement stmt = connection.createStatement()) {

            TrenoDAO trenoDB1 = new TrenoDAO();
            Treno treno1 = new Treno();
            treno1.setId_Treno("EC-7893");
            treno1.setTipologia("ECONOMY");
            treno1.setStato("In transito");
            treno1.setCarrozza(5);
            trenoDB1.aggiungiTreno(treno1);

            TrenoDAO trenoDB2 = new TrenoDAO();
            Treno treno2 = new Treno();
            treno2.setId_Treno("EC-7994");
            treno2.setTipologia("ECONOMY");
            treno2.setStato("In transito");
            treno2.setCarrozza(6);
            trenoDB2.aggiungiTreno(treno2);

            TrenoDAO trenoDB3 = new TrenoDAO();
            Treno treno3 = new Treno();
            treno3.setId_Treno("REG-5556");
            treno3.setTipologia("REGIONALE");
            treno3.setStato("In transito");
            treno3.setCarrozza(4);
            trenoDB3.aggiungiTreno(treno3);

            TrenoDAO trenoDB4 = new TrenoDAO();
            Treno treno4 = new Treno();
            treno4.setId_Treno("FA-8863");
            treno4.setTipologia("FRECCIARGENTO");
            treno4.setStato("In transito");
            treno4.setCarrozza(4);
            trenoDB4.aggiungiTreno(treno4);

            TrenoDAO trenoDB5 = new TrenoDAO();
            Treno treno5 = new Treno();
            treno5.setId_Treno("IC-7723");
            treno5.setTipologia("INTERCITY");
            treno5.setStato("In transito");
            treno5.setCarrozza(4);
            trenoDB5.aggiungiTreno(treno5);


            UtenteDAO utenteDB1 = new UtenteDAO();
            Utente utente1 = new Utente();
            utente1.setNomeUtente("Alice");
            utente1.setCognomeUtente("Bianchi");
            utente1.setDataNascitaUtente("28/01/1998");
            utente1.setIndirizzoUtente("Italia");
            utente1.setCFUtente("BIAALIE98A01T689C");
            utente1.setEmailUtente("alicebianchi98@gmail.com");
            utente1.setPasswordUtente("alicebianchi98");
            utente1.setAdmin(false);
            utente1.setCartaUtente(null);
            utenteDB1.aggiungiUtente(utente1);


            UtenteDAO utenteDB3 = new UtenteDAO();
            Utente utente3 = new Utente();
            utente3.setNomeUtente("Giuseppe");
            utente3.setCognomeUtente("Schipano");
            utente3.setDataNascitaUtente("03/01/2004");
            utente3.setIndirizzoUtente("Italia");
            utente3.setCFUtente("SCHGPP04A03F537C");
            utente3.setEmailUtente("peppeschipano@gmail.com");
            utente3.setPasswordUtente("peppeschipano04");
            utente3.setAdmin(true);
            utente3.setCartaUtente(null);
            utenteDB3.aggiungiUtente(utente3);

            FedeltaDAO fedeltaDAO1 = new FedeltaDAO();
            Fedelta fedelta1 = new Fedelta();
            fedelta1.setID("TESSERAFEDELTA1");
            fedelta1.setCFPossessoreTessera("FROMVRD76D09Q739P");
            fedelta1.setPunti(1);
            fedeltaDAO1.aggiungiTessera(fedelta1);

            UtenteDAO utenteDB2 = new UtenteDAO();
            Utente utente2 = new Utente();
            utente2.setNomeUtente("Francesco");
            utente2.setCognomeUtente("Verdi");
            utente2.setDataNascitaUtente("13/09/1976");
            utente2.setIndirizzoUtente("Italia");
            utente2.setCFUtente("FROMVRD76D09Q739P");
            utente2.setEmailUtente("francescoverdi@gmail.com");
            utente2.setPasswordUtente("francescoVerdi123");
            utente2.setAdmin(false);
            utente2.setCartaUtente(fedelta1);
            utenteDB2.aggiungiUtente(utente2);

            //Inserisco un utente con id_fedelta NULL all'inizio (mi serve per test updatefedelta su utenteTest)
            UtenteDAO utenteDB4 = new UtenteDAO();
            Utente utente4 = new Utente();
            utente4.setNomeUtente("Serena");
            utente4.setCognomeUtente("Esposito");
            utente4.setDataNascitaUtente("27/05/1973");
            utente4.setIndirizzoUtente("Italia");
            utente4.setCFUtente("SEATEPT47D09R739C");
            utente4.setEmailUtente("serenaesposito@gmail.com");
            utente4.setPasswordUtente("ciao1234");
            utente4.setAdmin(false);
            utenteDB4.aggiungiUtente(utente4);

            FedeltaDAO fedeltaDB3 = new FedeltaDAO();
            Fedelta fedelta3 = new Fedelta();
            fedelta3.setID("TESSERAFEDELTA3");
            fedelta3.setCFPossessoreTessera("SEATEPT47D09R739C");
            fedelta3.setPunti(3);
            fedeltaDB3.aggiungiTessera(fedelta3);

            TrattaDAO trattaDB2 = new TrattaDAO();
            Tratta tratta2 = new Tratta();
            tratta2.setId_tratta("TRATTA2");
            tratta2.setId_treno("EC-7994");
            tratta2.setData("12/05/2025");
            tratta2.setOraPartenza("14:58:40");
            tratta2.setOraArrivo("16:48:00");
            tratta2.setStazionePartenza("Gioia Tauro");
            tratta2.setStazioneArrivo("Cosenza");
            tratta2.setNumeroPostiDisponibili(10);
            tratta2.setClassiDisponibili("FIRST");
            tratta2.setPrezzo(11.90);
            trattaDB2.aggiungiTratta(tratta2);

            TrattaDAO trattaDB3 = new TrattaDAO();
            Tratta tratta3 = new Tratta();
            tratta3.setId_tratta("TRATTA3");
            tratta3.setId_treno("REG-5556");
            tratta3.setData("15/07/2025");
            tratta3.setOraPartenza("17:51:00");
            tratta3.setOraArrivo("18:26:30");
            tratta3.setStazionePartenza("Paola");
            tratta3.setStazioneArrivo("Lamezia Terme");
            tratta3.setNumeroPostiDisponibili(43);
            tratta3.setClassiDisponibili("STANDARD");
            tratta3.setPrezzo(4.70);
            trattaDB3.aggiungiTratta(tratta3);

            TrattaDAO trattaDB4 = new TrattaDAO();
            Tratta tratta4 = new Tratta();
            tratta4.setId_tratta("TRATTA4");
            tratta4.setId_treno("IC-7723");
            tratta4.setData("20/10/2025");
            tratta4.setOraPartenza("09:50:00");
            tratta4.setOraArrivo("15:58:00");
            tratta4.setStazionePartenza("Napoli Centrale");
            tratta4.setStazioneArrivo("Reggio Di Calabria Lido");
            tratta4.setNumeroPostiDisponibili(56);
            tratta4.setClassiDisponibili("BUSINESS");
            tratta4.setPrezzo(52.10);
            trattaDB4.aggiungiTratta(tratta4);

            TrattaDAO trattaDB5 = new TrattaDAO();
            Tratta tratta5 = new Tratta();
            tratta5.setId_tratta("TRATTA5");
            tratta5.setId_treno("FA-8863");
            tratta5.setData("26/12/2025");
            tratta5.setOraPartenza("05:58:50");
            tratta5.setOraArrivo("15:25:00");
            tratta5.setStazionePartenza("Roma Termini");
            tratta5.setStazioneArrivo("Catanzaro Lido");
            tratta5.setNumeroPostiDisponibili(86);
            tratta5.setClassiDisponibili("FIRST");
            tratta5.setPrezzo(54.90);
            trattaDB5.aggiungiTratta(tratta5);

            TrattaDAO trattaDB6 = new TrattaDAO();
            Tratta tratta6 = new Tratta();
            tratta6.setId_tratta("TRATTA6");
            tratta6.setId_treno("RE-5560");
            tratta6.setData("19/11/2025");
            tratta6.setOraPartenza("12:55:00");
            tratta6.setOraArrivo("13:48:00");
            tratta6.setStazionePartenza("Rosarno");
            tratta6.setStazioneArrivo("Amantea");
            tratta6.setNumeroPostiDisponibili(26);
            tratta6.setClassiDisponibili("STANDARD");
            tratta6.setPrezzo(8.50);
            trattaDB6.aggiungiTratta(tratta6);


            PromozioneDAO promozioneDB = new PromozioneDAO();
            Promozione promozione = new Promozione();
            promozione.setCodicePromo("PROMO456");
            promozione.setPercentualeSconto(15);
            promozione.setTipoTreno("ECONOMY");
            promozione.setInizioPromo("12/05/2025");
            promozione.setFinePromo("14/11/2025");
            promozione.setSoloFedelta(false);
            promozioneDB.addPromozione(promozione);

            PrenotazioneDAO prenotazioneDB = new PrenotazioneDAO();
            Prenotazione prenotazione = new Prenotazione();
            prenotazione.setId_Prenotazione("PRE2549735126794");
            prenotazione.setPostoPrenotazione(22);
            prenotazione.setDataScadenza("14/07/2025");
            prenotazione.setCarrozza(1);
            prenotazione.setId_tratta("TRATTA3");
            prenotazione.setCFUtente("BIAALIE98A01T689C");
            prenotazioneDB.aggiungiPrenotazione(prenotazione) ;

            BigliettoDAO bigliettoDB = new BigliettoDAO();
            Biglietto biglietto = new Biglietto();
            biglietto.setId_prenotazione(null);
            biglietto.setId_Biglietto("FA1750265026411");
            biglietto.setCF("BIAALIE98A01T689C");
            biglietto.setClasse("SECONDA CLASSE");
            biglietto.setPosto(85);
            biglietto.setCarrozza(7);
            biglietto.setId_tratta("TRATTA5");
            bigliettoDB.aggiungiBiglietto(biglietto);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void pulisciDatabase() {
        try{
            InitDB.init();

        try (Connection conn = DBConnectionSingleton.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");
            stmt.execute("DELETE FROM biglietti");
            stmt.execute("DELETE FROM prenotazioni");
            stmt.execute("DELETE FROM tratta");
            stmt.execute("DELETE FROM promozioni");
            stmt.execute("DELETE FROM fedelta");
            stmt.execute("DELETE FROM utenti");
            stmt.execute("DELETE FROM treni");
            stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");
        }
            popola();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore" +e.getMessage());

        }
    }
}
