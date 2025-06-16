package org.example.servergRPC;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class TrenicalServerMain {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50051)
                .addService(new TrenicalServiceImpl())
                .build();

        server.start();
        System.out.println("Server gRPC avviato sulla porta 50051.");
        server.awaitTermination();
    }
}
