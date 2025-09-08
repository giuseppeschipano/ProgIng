package org.example.servergRPC;

import org.example.dao.*;
import org.example.grpc.TrenicalServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.example.grpc.*;
import org.example.model.*;
import org.example.persistence.DBConnectionSingleton;
import org.example.service.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class TrenicalServiceImpl extends TrenicalServiceGrpc.TrenicalServiceImplBase {

    private final UtenteService utenteService = new UtenteService();
    private final TrattaService trattaService = new TrattaService();
    private final TrenoService trenoService = new TrenoService();
    private final BiglietteriaService biglietteriaService = new BiglietteriaService(new BigliettoDAO(), new TrattaService());
    private final FedeltaService fedeltaService = new FedeltaService();
    private final PrenotazioneService prenotazioneService = new PrenotazioneService(new PrenotazioneDAO(), new BigliettoDAO(), new TrattaDAO());
    private final PromozioneService promozioneService = new PromozioneService();

    private final Map<String, List<StreamObserver<NotificaTrenoResponse>>> iscrittiPerTreno = new ConcurrentHashMap<>();


    @Override
    public void cercaTratte(CercaTratteRequest request, StreamObserver<CercaTratteResponse> responseObserver) {
        String partenza = request.getStazionePartenza();
        String arrivo = request.getStazioneArrivo();
        String data = request.getData();
        String classe = request.getClasse();
        List<Tratta> tratte = this.trattaService.cercaTratte(partenza, arrivo, data, classe);
        CercaTratteResponse.Builder responseBuilder = CercaTratteResponse.newBuilder();
        for (Tratta tratta : tratte) {
            TrattaDTO trattaDTO = TrattaDTO.newBuilder()
                    .setIdTratta(tratta.getId_tratta())
                    .setOraPartenza(tratta.getOraPartenza())
                    .setOraArrivo(tratta.getOraArrivo())
                    .setStazionePartenza(tratta.getStazionePartenza())
                    .setStazioneArrivo(tratta.getStazioneArrivo())
                    .setNumeroPostiDisponibili(tratta.getNumeroPostiDisponibili())
                    .setPrezzo(tratta.getPrezzo())
                    .setClassiDisponibili(tratta.getClassiDisponibili())
                    .build();
            responseBuilder.addTratte(trattaDTO);
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }


    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        String email = request.getEmail();
        String password = request.getPassword();
        Utente utente = utenteService.login(email, password);
        boolean isAdmin = utenteService.loginAdmin(email, password) != null;
        if (utente != null) {
            UserDTO.Builder userBuilder = UserDTO.newBuilder()
                    .setNome(utente.getNomeUtente())
                    .setCognome(utente.getCognomeUtente())
                    .setCf(utente.getCFUtente())
                    .setIndirizzo(utente.getIndirizzoUtente())
                    .setDataNascita(utente.getDataNascitaUtente());

            // Aggiungi tessera fedeltà se presente
            if (utente.getCartaUtente() != null) {
                FedeltaDTO fedeltaDTO = FedeltaDTO.newBuilder()
                        .setId(utente.getCartaUtente().getID())
                        .setPunti(utente.getCartaUtente().getPunti())
                        .build();
                userBuilder.setFedelta(fedeltaDTO);
            }
            LoginResponse response = LoginResponse.newBuilder()
                    .setUtente(userBuilder.build())
                    .setSuccesso(true)
                    .setMessaggio("Login effettuato con successo")
                    .setIsAdmin(isAdmin)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            LoginResponse response = LoginResponse.newBuilder()
                    .setSuccesso(false)
                    .setMessaggio("Email o password errati")
                    .setIsAdmin(false)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }


    @Override
    public void registrazione (RegistrazioneRequest request, StreamObserver<RegistrazioneResponse> responseObserver) {
        String nome = request.getNome();
        String cognome = request.getCognome();
        String email = request.getEmail();
        String password = request.getPassword();
        String cf = request.getCf();
        String indirizzo = request.getIndirizzo();
        String dataNascita = request.getDataNascita();
        Utente nuovoUtente = new Utente();
        nuovoUtente.setNomeUtente(nome);
        nuovoUtente.setCognomeUtente(cognome);
        nuovoUtente.setEmailUtente(email);
        nuovoUtente.setPasswordUtente(password);
        nuovoUtente.setCFUtente(cf);
        nuovoUtente.setIndirizzoUtente(indirizzo);
        nuovoUtente.setDataNascitaUtente(dataNascita);
        boolean success = false;
        String messaggio;
        try{
            UtenteDAO utenteDAO = new UtenteDAO();
            if (utenteDAO.getUtenteByCF(cf) != null){
                messaggio = "Utente con questo codice fiscale già presente";
            } else{
                utenteDAO.aggiungiUtente(nuovoUtente);
                success = true;
                messaggio = "Registrazione completata";
            }
        } catch (Exception e){
            e.printStackTrace();
            messaggio = "Errore durante la registrazione : " + e.getMessage() ;
        }
        RegistrazioneResponse response = RegistrazioneResponse.newBuilder()
                .setSuccess(success).setMessaggio(messaggio).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }


@Override
public void prenota(PrenotazioneRequest request, StreamObserver<PrenotazioneResponse> responseObserver) {
    String cf = request.getCf();
    String idTratta = request.getIdTratta();
    int postoRichiesto = request.getPosto();
    int carrozzaRichiesta = request.getCarrozza();
    prenotazioneService.controllaPrenotazioniScadute();
    Prenotazione prenotazione = new Prenotazione();
    prenotazione.setCFUtente(cf);
    prenotazione.setId_tratta(idTratta);
    prenotazione.setId_Prenotazione("PRE" + System.currentTimeMillis());
    LocalDate scadenza = LocalDate.now().plusDays(1);
    prenotazione.setDataScadenza(scadenza.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    boolean success = false;
    String messaggio;
    try (Connection conn = DBConnectionSingleton.getConnection()) {
        conn.setAutoCommit(false);

        // Trova o assegna posto
        if (postoRichiesto == 0 && carrozzaRichiesta == 0) {
            int[] postoCarrozza = prenotazioneService.trovaPostoDisponibile(idTratta);
            if (postoCarrozza == null) {
                messaggio = "Non ci sono posti disponibili per questa tratta.";
            } else {
                prenotazione.setPostoPrenotazione(postoCarrozza[0]);
                prenotazione.setCarrozza(postoCarrozza[1]);
                prenotazioneService.effettuaPrenotazione(prenotazione);
                success = true;
                messaggio = "Prenotazione effettuata con successo.";
            }
        } else {
            // posto specificato dal client: verifica eventuale posto occupato
            boolean occupato = prenotazioneService.postoGiaOccupato(idTratta, carrozzaRichiesta, postoRichiesto);
            if (occupato) {
                messaggio = "Il posto selezionato è già occupato.";
            } else {
                prenotazione.setPostoPrenotazione(postoRichiesto);
                prenotazione.setCarrozza(carrozzaRichiesta);
                prenotazioneService.effettuaPrenotazione(prenotazione);
                success = true;
                messaggio = "Prenotazione effettuata con successo.";
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        success = false;
        messaggio = "Errore nella prenotazione: " + e.getMessage();
    }
    PrenotazioneResponse response = PrenotazioneResponse.newBuilder()
            .setIdPrenotazione(success ? prenotazione.getId_Prenotazione() : "")
            .setPostoPrenotazione(success ? prenotazione.getPostoPrenotazione() : 0)
            .setCarrozza(success ? prenotazione.getCarrozza() : 0)
            .setSuccess(success)
            .setMessaggio(messaggio)
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
}

    @Override
    public void acquistaBiglietto(AcquistoRequest request, StreamObserver<AcquistoResponse> respObs) {
        AcquistoResponse.Builder response = AcquistoResponse.newBuilder();
        try {
            String cf = request.getCf();
            String idTratta = request.getIdTratta();
            String classe = request.getClasse();
            int posto = request.getPosto();
            int carrozza = request.getCarrozza();
            String idPrenotazione = request.getPrenotazioneId();  //Per l'acquisto si può passare dalla prenotazione o acquistare direttamente
            if (idPrenotazione.isEmpty()) {
                idPrenotazione = null;
            }
            String numeroCarta = request.getNumeroCarta();
            // Verifica della carta (Luhn)
            if (!BiglietteriaService.isValidCardNumber(numeroCarta)) {
                response.setSuccess(false).setMessaggio("Pagamento rifiutato: numero carta non valido.");
                respObs.onNext(response.build());
                respObs.onCompleted();
                return;
            }
            // Recupero della tratta e tipo treno per generare id biglietto
            Tratta tratta = trattaService.getTrattaByID(idTratta);
            String tipoTreno = trenoService.getTrenoById(tratta.getId_treno()).getTipologia();
            String prefix = switch (tipoTreno.toUpperCase()) {
                case "FRECCIAROSSA", "FR" -> "FR";
                case "FRECCIARGENTO", "FA" -> "FA";
                case "FRECCIABIANCA", "FB" -> "FB";
                case "INTERCITY", "IC" -> "IC";
                case "ECONOMY", "EC" -> "EC";
                case "REGIONALEVELOCE", "RV" -> "RV";
                case "REGIONALE", "R", "REG" -> "RG";
                default -> tipoTreno.toUpperCase().substring(0, Math.min(3, tipoTreno.length()));
            };
            String idBiglietto = prefix + System.currentTimeMillis();
            Biglietto biglietto = new Biglietto(
                    idBiglietto, classe, idPrenotazione, idTratta, cf, posto, carrozza
            );
            boolean successo;
            if (idPrenotazione != null && !idPrenotazione.isEmpty()) {
                // Conferma prenotazione se valida
                Prenotazione p = prenotazioneService.getPrenotazioneById(idPrenotazione);
                if (p == null) {
                    response.setSuccess(false).setMessaggio("Prenotazione non trovata.");
                    respObs.onNext(response.build());
                    respObs.onCompleted();
                    return;
                }
                LocalDate scadenza = LocalDate.parse(
                        p.getDataScadenza(),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                );
                if (LocalDate.now().isAfter(scadenza)) {
                    response.setSuccess(false).setMessaggio("Prenotazione scaduta.");
                    respObs.onNext(response.build());
                    respObs.onCompleted();
                    return;
                }
                prenotazioneService.confermaPrenotazione(p, biglietto);
                successo = true;
            } else {
                // Acquisto diretto
                successo = biglietteriaService.acquistaBiglietti(List.of(biglietto));
            }
            if (!successo) {
                response.setSuccess(false).setMessaggio("Acquisto fallito.");
            } else {
                // Aggiorna fedeltà
                if (fedeltaService.hasTessera(cf)) {
                    fedeltaService.incrementaPunti(cf, 1);
                }
                BigliettoDTO bigliettoDTO = BigliettoDTO.newBuilder()
                        .setIdBiglietto(biglietto.getId_Biglietto())
                        .setClasse(biglietto.getClasse())
                        .setIdTratta(biglietto.getId_tratta())
                        .setPosto(biglietto.getPosto())
                        .setCarrozza(biglietto.getCarrozza())
                        .build();
                response.setSuccess(true)
                        .setMessaggio("Acquisto completato.")
                        .setBiglietto(bigliettoDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false).setMessaggio("Errore nell'acquisto");
        }
        respObs.onNext(response.build());
        respObs.onCompleted();
    }

    //Nuova tessera (ovviamente univoca per ogni utente)
    @Override
    public void sottoscriviFedelta(SottoscrizioneFedeltaRequest request, StreamObserver<SottoscrizioneFedeltaResponse> responseObserver) {
        String cf = request.getCf();
        FedeltaService fedeltaService = new FedeltaService();
        SottoscrizioneFedeltaResponse.Builder responseBuilder = SottoscrizioneFedeltaResponse.newBuilder();
        try {
            if (fedeltaService.hasTessera(cf)) {
                responseBuilder
                        .setSuccess(false)
                        .setMessaggio("Hai già una tessera fedeltà.");
            } else {
                Fedelta nuova = new Fedelta("FDL" + System.currentTimeMillis(), cf, 0);
                fedeltaService.aggiungiTessera(nuova);
                FedeltaDTO tesseraDTO = FedeltaDTO.newBuilder()
                        .setId(nuova.getID())
                        .setPunti(nuova.getPunti())
                        .build();
                responseBuilder
                        .setSuccess(true)
                        .setMessaggio("Tessera fedeltà creata con successo.")
                        .setTessera(tesseraDTO);
            }
        } catch (Exception e) {
            responseBuilder
                    .setSuccess(false)
                    .setMessaggio("Errore durante la sottoscrizione: " + e.getMessage());
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }


    @Override
    public void ottieniPromozioni(PromozioniRequest request, StreamObserver<PromozioniResponse> responseObserver) {
        String cf = request.getCf();
        PromozioneService promoService = new PromozioneService();
        List<Promozione> promoList = promoService.getPromozioniPerUtente(cf);
        PromozioniResponse.Builder responseBuilder = PromozioniResponse.newBuilder();
        for (Promozione p : promoList) {
            PromozioneDTO dto = PromozioneDTO.newBuilder()
                    .setCodicePromo(p.getCodicePromo())
                    .setPercentualeSconto(p.getPercentualeSconto())
                    .setTipoTreno(p.getTipoTreno())
                    .setInizioPromo(p.getInizioPromo())
                    .setFinePromo(p.getFinePromo())
                    .setSoloFedelta(p.isSoloFedelta())
                    .build();
            responseBuilder.addPromozioni(dto);
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }


    @Override
    public void modificaBiglietto(ModificaBigliettoRequest request, StreamObserver<ModificaBigliettoResponse> responseObserver) {
        String idBiglietto = request.getIdBiglietto();
        String nuovaData = request.getNuovaData();
        String nuovaOra = request.getNuovaOra();
        String nuovaClasse = request.getNuovaClasse();
        boolean conferma = request.getConferma();
        ModificaBigliettoResponse.Builder responseBuilder = ModificaBigliettoResponse.newBuilder();
        try {
            BigliettoDAO bigliettoDAO = new BigliettoDAO();
            // Recupero biglietto originale
            Biglietto bigliettoOriginale = bigliettoDAO.getBigliettoPerID(idBiglietto);
            if (bigliettoOriginale == null) {
                responseBuilder.setSuccess(false).setMessaggio("Biglietto non trovato");
                responseObserver.onNext(responseBuilder.build());
                responseObserver.onCompleted();
                return;
            }
            // Recupero tratta originale
            Tratta trattaOriginale = trattaService.getTrattaByID(bigliettoOriginale.getId_tratta());
            if (trattaOriginale == null) {
                responseBuilder.setSuccess(false).setMessaggio("Tratta originale non trovata");
                responseObserver.onNext(responseBuilder.build());
                responseObserver.onCompleted();
                return;
            }
            //Filtro solo per nuova data e nuova ora (stazioni non per forza uguali alle originali)
            Tratta nuovaTratta = trattaService.getAllTratte().stream()
                    .filter(t ->
                            t.getData().equals(nuovaData) &&
                            t.getOraPartenza().equals(nuovaOra)
                    )
                    .findFirst()
                    .orElse(null);
            if (nuovaTratta == null) {
                responseBuilder.setSuccess(false).setMessaggio("Nessuna tratta disponibile per la nuova data/ora");
                responseObserver.onNext(responseBuilder.build());
                responseObserver.onCompleted();
                return;
            }
            // Calcolo differenza prezzo
            double prezzoOriginale = trattaOriginale.getPrezzo();
            double prezzoNuovo = nuovaTratta.getPrezzo();
            double differenza = prezzoNuovo - prezzoOriginale;
            Biglietto bigliettoAggiornato = null;
            if (conferma) {
                boolean aggiornato = biglietteriaService.aggiornaBiglietto(idBiglietto, nuovaTratta.getId_tratta(), nuovaClasse);
                if (aggiornato) {
                    bigliettoAggiornato = bigliettoDAO.getBigliettoPerID(idBiglietto);
                } else {
                    responseBuilder.setSuccess(false).setMessaggio("Errore durante aggiornamento biglietto");
                    responseObserver.onNext(responseBuilder.build());
                    responseObserver.onCompleted();
                    return;
                }
            }
            BigliettoDTO bigliettoDTO = (bigliettoAggiornato != null)
                    ? BigliettoDTO.newBuilder()
                    .setIdBiglietto(bigliettoAggiornato.getId_Biglietto())
                    .setClasse(bigliettoAggiornato.getClasse())
                    .setIdTratta(bigliettoAggiornato.getId_tratta())
                    .setPosto(bigliettoAggiornato.getPosto())
                    .setCarrozza(bigliettoAggiornato.getCarrozza())
                    .build()
                    : BigliettoDTO.getDefaultInstance();
            responseBuilder
                    .setSuccess(true)
                    .setMessaggio(conferma ? "Biglietto aggiornato con successo" : "Simulazione completata")
                    .setDifferenzaPrezzo(differenza)
                    .setBigliettoAggiornato(bigliettoDTO);
        } catch (Exception e) {
            e.printStackTrace();
            responseBuilder.setSuccess(false).setMessaggio("Errore durante la modifica del biglietto");
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }


    @Override
    public void riceviNotificheTreno(TrenoNotificaRequest request, StreamObserver<NotificaTrenoResponse> responseObserver) {
        String cf = request.getCf();
        String idTreno = request.getIdTreno();
        //Verifica sull'esistenza di treno e cf
        if (utenteService.getUtente(cf) == null) {
            System.out.println("Utente non trovato: " + cf);
            NotificaTrenoResponse erroreCF = NotificaTrenoResponse.newBuilder()
                    .setStato("ERRORE")
                    .setMessaggio("Utente non trovato")
                    .build();
            responseObserver.onNext(erroreCF);
            responseObserver.onCompleted();
            return;
        }
        if (trenoService.getTrenoById(idTreno) == null) {
            System.out.println("Treno non trovato: " + idTreno);
            NotificaTrenoResponse erroreTreno = NotificaTrenoResponse.newBuilder()
                    .setIdTreno(idTreno)
                    .setStato("ERRORE")
                    .setMessaggio("Treno non trovato")
                    .build();
            responseObserver.onNext(erroreTreno);
            responseObserver.onCompleted();
            return;

        }
        System.out.println("Utente " + cf + " si è iscritto alle notifiche per treno " + idTreno);
        synchronized (this) {
            iscrittiPerTreno.computeIfAbsent(idTreno, k -> new ArrayList<>()).add(responseObserver);
        }
    }


    @Override
    public void statoAttualeTreno(TrenoNotificaRequest request, StreamObserver<NotificaTrenoResponse> responseObserver)  {
        String idTreno = request.getIdTreno();
        TrenoDAO trenoDAO = new TrenoDAO();
        TrattaDAO trattaDAO = new TrattaDAO();
        Treno treno = trenoDAO.getTrenoById(idTreno);
        if (treno == null) {
            NotificaTrenoResponse response = NotificaTrenoResponse.newBuilder()
                    .setIdTreno(idTreno + "\n")
                    .setStato("SCONOSCIUTO" + "\n")
                    .setMessaggio("Treno non trovato." + "\n")
                    .setOrarioStimato("N/A")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }
        Tratta tratta = trattaDAO.getTrattaByIdTreno(idTreno);
        String orarioArrivo;
        if (tratta != null && tratta.getOraArrivo() != null) {
            orarioArrivo = tratta.getOraArrivo();
        } else {
            orarioArrivo = "N/A";
        }
        NotificaTrenoResponse response =NotificaTrenoResponse.newBuilder()
                .setIdTreno(idTreno + "\n")
                .setStato((treno.getStato() != null ? treno.getStato() : "SCONOSCIUTO") + "\n")
                .setMessaggio("Treno partito da " + tratta.getStazionePartenza() + "\n")
                .setOrarioStimato(orarioArrivo)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public void fedeltaNotificaPromoOfferte(AccettiNotificaFedRequest request, StreamObserver<AccettiNotificaFedResponse> responseObserver) {
        String cf = request.getCf();
        boolean desideraContatto = request.getDesideroContatto();
        AccettiNotificaFedResponse.Builder responseBuilder = AccettiNotificaFedResponse.newBuilder();
        if (!desideraContatto) {
            responseBuilder.setMessage("Hai scelto di non ricevere promozioni personalizzate.");
        } else {
            List<Promozione> promozioni = promozioneService.getPromozioniPerUtente(cf);
            if (!promozioni.isEmpty()) {
                int randomIndex = ThreadLocalRandom.current().nextInt(promozioni.size()); //casualmente tra quelle che ho inserito nel database
                Promozione migliore = promozioni.get(randomIndex);
                PromozioneDTO dto = PromozioneDTO.newBuilder()
                        .setCodicePromo(migliore.getCodicePromo())
                        .setPercentualeSconto(migliore.getPercentualeSconto())
                        .setTipoTreno(migliore.getTipoTreno())
                        .setInizioPromo(migliore.getInizioPromo())
                        .setFinePromo(migliore.getFinePromo())
                        .setSoloFedelta(migliore.isSoloFedelta())
                        .build();
                responseBuilder
                        .setPromoInArrivo(dto)
                        .setMessage("Ecco una promozione riservata a te.");
            } else {
                responseBuilder.setMessage("Al momento non ci sono promozioni disponibili per te.");
            }
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}