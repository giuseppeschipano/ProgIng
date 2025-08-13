package org.example.clientgRPC;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.example.grpc.*;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TrenicalClientImpl {

    private final ManagedChannel channel;
    private final TrenicalServiceGrpc.TrenicalServiceBlockingStub blockingStub;
    private final TrenicalServiceGrpc.TrenicalServiceStub asyncStub;


    public TrenicalClientImpl(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = TrenicalServiceGrpc.newBlockingStub(channel);
        this.asyncStub = TrenicalServiceGrpc.newStub(channel);

        // DEBUG
        System.out.println("[DEBUG CLIENT] Connessione gRPC inizializzata -> " + host + ":" + port);
    }

    // Chiusura del canale gRPC
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
            try {
                if (!channel.awaitTermination(5, TimeUnit.SECONDS)) {
                    channel.shutdownNow();
                }
            } catch (InterruptedException e) {
                channel.shutdownNow();
            }
        }
    }


    //Invia una richiesta al server per cercare le tratte disponibili
    // tra due stazioni in una data e classe specificata.
    public CercaTratteResponse cercaTratte(String stazionePartenza, String stazioneArrivo, String data, String classe) {
        CercaTratteRequest request = CercaTratteRequest.newBuilder()
                .setStazionePartenza(stazionePartenza)
                .setStazioneArrivo(stazioneArrivo)
                .setData(data)
                .setClasse(classe)
                .build();
        return  blockingStub.cercaTratte(request);
    }


    // Chiamata al metodo Login del server gRPC
    public LoginResponse provaLogin(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return LoginResponse.newBuilder()
                    .setSuccesso(false)
                    .setMessaggio("Email e password obbligatorie")
                    .build();
        }
        LoginRequest request = LoginRequest.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .build();
        try {
            return blockingStub.login(request);
        } catch (StatusRuntimeException e) {
            System.out.println("Errore durante il login: " + e.getStatus().getDescription());
            return LoginResponse.newBuilder()
                    .setSuccesso(false)
                    .setMessaggio("Errore durante il login: " + e.getStatus().getDescription())
                    .build();
        }
    }

    // Chiamata al metodo Registrazione del server gRPC
    public RegistrazioneResponse registrazione(String nome, String cognome, String email, String password, String cf, String indirizzo, String dataNascita) {
        if (nome == null || nome.isEmpty() ||
                cognome == null || cognome.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty() ||
                cf == null || cf.isEmpty() ||
                indirizzo == null || indirizzo.isEmpty() ||
                dataNascita == null || dataNascita.isEmpty()) {
            return RegistrazioneResponse.newBuilder()
                    .setSuccess(false)
                    .setMessaggio("Compilare tutti i campi!")
                    .build();
        }
        RegistrazioneRequest request = RegistrazioneRequest.newBuilder()
                .setNome(nome)
                .setCognome(cognome)
                .setEmail(email)
                .setPassword(password)
                .setCf(cf)
                .setIndirizzo(indirizzo)
                .setDataNascita(dataNascita)
                .build();
        try {
            return blockingStub.registrazione(request);
        } catch (StatusRuntimeException e) {
            System.out.println("Errore durante la registrazione: " + e.getStatus().getDescription());
            return RegistrazioneResponse.newBuilder()
                    .setSuccess(false)
                    .setMessaggio("Errore durante la registrazione: " + e.getStatus().getDescription())
                    .build();
        }
    }


    // Chiamata al metodo Prenotazione del server gRPC(restituisce la risposta PrenotazioneResponse ricevuta dal server)
    public PrenotazioneResponse prenota(String cf, String idTratta) {
        PrenotazioneRequest request = PrenotazioneRequest.newBuilder()
                .setCf(cf)
                .setIdTratta(idTratta)
                .setPosto(0)
                .setCarrozza(0)
                .build();
        try {
            PrenotazioneResponse response = blockingStub.prenota(request);
            if (!response.getSuccess()) {
                System.out.println("Prenotazione fallita: " + response.getMessaggio());
            } else{
                System.out.println("Prenotazione effettuata con successo!");
                System.out.println("ID Prenotazione: " + response.getIdPrenotazione());
                System.out.println("Posto assegnato: " + response.getPostoPrenotazione());
                System.out.println("Carrozza assegnata: " + response.getCarrozza());
            }
            return response;
        } catch (StatusRuntimeException e) {
            System.out.println("Errore durante la prenotazione: " + e.getStatus().getDescription());
            return PrenotazioneResponse.newBuilder()
                    .setSuccess(false)
                    .setIdPrenotazione("")
                    .setMessaggio("Errore durante la prenotazione: " + e.getStatus().getDescription())
                    .build();
        }
    }

    // Invia al server richiesta di acquisto di un biglietto
    public AcquistoResponse acquistaBiglietto(UserDTO utente, String cf, String idTratta, String classe, int posto, int carrozza, String numeroCarta, String prenotazioneId) {
        AcquistoRequest.Builder requestBuilder = AcquistoRequest.newBuilder()
                .setUtente(utente)
                .setCf(cf)
                .setIdTratta(idTratta)
                .setClasse(classe)
                .setPosto(posto)
                .setCarrozza(carrozza)
                .setNumeroCarta(numeroCarta);
        if (prenotazioneId != null && !prenotazioneId.isEmpty()) {
            requestBuilder.setPrenotazioneId(prenotazioneId);
        }
        try {
            return blockingStub.acquistaBiglietto(requestBuilder.build());
        } catch (StatusRuntimeException e) {
            System.out.println("Errore durante l'acquisto: " + e.getStatus().getDescription());
            return AcquistoResponse.newBuilder()
                    .setSuccess(false)
                    .setMessaggio("Errore durante l'acquisto: " + e.getStatus().getDescription())
                    .build();
        }
    }

    //Creazione tessera fedeltà per l'utente cf
    public SottoscrizioneFedeltaResponse sottoscriviFedelta(String cf) {
        SottoscrizioneFedeltaRequest request = SottoscrizioneFedeltaRequest.newBuilder()
                .setCf(cf)
                .build();
        try {
            return blockingStub.sottoscriviFedelta(request);
        } catch (StatusRuntimeException e) {
            System.out.println("Errore durante la sottoscrizione : " + e.getStatus().getDescription());
            return SottoscrizioneFedeltaResponse.newBuilder()
                    .setSuccess(false)
                    .setMessaggio("Errore durante la sottoscrizione: " + e.getStatus().getDescription())
                    .build();
        }
    }

    // Elenco promozioni disponibili per l'utente cf
    public List<PromozioneDTO> ottieniPromozioni(String cf) {
        PromozioniRequest request = PromozioniRequest.newBuilder()
                .setCf(cf)
                .build();
        try {
            PromozioniResponse response = blockingStub.ottieniPromozioni(request);
            return response.getPromozioniList();
        } catch (StatusRuntimeException e) {
            System.out.println("Errore durante il recupero delle promozioni: " + e.getStatus().getDescription());
            return Collections.emptyList();
        }
    }

    public ModificaBigliettoResponse modificaBiglietto(String idBiglietto, String nuovaData, String nuovaOra, String nuovaClasse, boolean conferma) {
        ModificaBigliettoRequest request = ModificaBigliettoRequest.newBuilder()
                .setIdBiglietto(idBiglietto)
                .setNuovaData(nuovaData)
                .setNuovaOra(nuovaOra)
                .setNuovaClasse(nuovaClasse)
                .setConferma(conferma)
                .build();
        try {
            return blockingStub.modificaBiglietto(request);
        } catch (StatusRuntimeException e) {
            System.out.println("Errore durante la modifica del biglietto: " + e.getStatus().getDescription());
            return ModificaBigliettoResponse.newBuilder()
                    .setSuccess(false)
                    .setMessaggio("Errore durante la modifica del biglietto: " + e.getStatus().getDescription())
                    .build();
        }
    }

    //Ricevo le notifiche sullo stato del treno in tempo reale
    public void riceviNotificheTreno(String cf, String idTreno, io.grpc.stub.StreamObserver<NotificaTrenoResponse> observer) {
        TrenoNotificaRequest request = TrenoNotificaRequest.newBuilder()
                .setCf(cf)
                .setIdTreno(idTreno)
                .build();

        asyncStub.riceviNotificheTreno(request, observer);
    }

    //Richiesta dello stato del treno idTreno
    public NotificaTrenoResponse statoAttualeTreno(String cf, String idTreno) {
        TrenoNotificaRequest request = TrenoNotificaRequest.newBuilder()
                .setCf(cf)
                .setIdTreno(idTreno)
                .build();
        try {
            return blockingStub.statoAttualeTreno(request);
        } catch (io.grpc.StatusRuntimeException e) {
            System.err.println("Errore nel recupero stato treno: " + e.getStatus().getDescription());
            return NotificaTrenoResponse.newBuilder()
                    .setIdTreno(idTreno)
                    .setStato("ERRORE")
                    .setMessaggio("Errore: " + e.getStatus().getDescription())
                    .setOrarioStimato("N/A")
                    .build();
        }
    }

        public AccettiNotificaFedResponse notificaPromoFedelta (String cf,boolean desideroContatto){
            AccettiNotificaFedRequest request = AccettiNotificaFedRequest.newBuilder()
                    .setCf(cf)
                    .setDesideroContatto(desideroContatto)
                    .build();
            try {
                return blockingStub.fedeltaNotificaPromoOfferte(request);
            } catch (StatusRuntimeException e) {
                System.out.println("Errore durante la richiesta di notifica promozioni: " + e.getStatus().getDescription());
                return AccettiNotificaFedResponse.newBuilder()
                        .setMessage("Errore: " + e.getStatus().getDescription())
                        .build();
            }
        }
    }


