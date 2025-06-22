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


    public TrenicalClientImpl(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = TrenicalServiceGrpc.newBlockingStub(channel);
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
    public List<TrattaDTO> cercaTratte(String stazionePartenza, String stazioneArrivo, String data, String classe) {
        CercaTratteRequest request = CercaTratteRequest.newBuilder()
                .setStazionePartenza(stazionePartenza)
                .setStazioneArrivo(stazioneArrivo)
                .setData(data)
                .setClasse(classe)
                .build();

        CercaTratteResponse response;
        try {
            response = blockingStub.cercaTratte(request);
        } catch (StatusRuntimeException e) {
            System.out.println("Errore durante la ricerca delle tratte: " + e.getStatus().getDescription());
            return Collections.emptyList();
        }

        return response.getTratteList();
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
    public PrenotazioneResponse prenota(String cf, String idTratta, int posto, int carrozza) {
        PrenotazioneRequest request = PrenotazioneRequest.newBuilder()
                .setCf(cf)
                .setIdTratta(idTratta)
                .setPosto(posto)
                .setCarrozza(carrozza)
                .build();

        try {
            PrenotazioneResponse response = blockingStub.prenota(request);

            if (!response.getSuccess()) {
                System.out.println("Prenotazione fallita: " + response.getMessaggio());
                return response;
            }
            return  response;

        } catch (StatusRuntimeException e) {
            System.out.println("Errore durante la prenotazione: " + e.getStatus().getDescription());
            return PrenotazioneResponse.newBuilder()
                    .setSuccess(false)
                    .setIdPrenotazione("")
                    .setMessaggio("Errore durante la prenotazione: " + e.getStatus().getDescription())
                    .build();
        }
    }
}


