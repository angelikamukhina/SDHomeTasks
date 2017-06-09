package ru.spbau.mit;

import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.Scanner;

public class Server extends User {
    private static final Logger logger = LogManager.getLogger(Server.class);

    private Server() {
        logger.info("Starting the grpc server");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        userName = scanner.nextLine();
        System.out.println("Enter port: ");
        int port = scanner.nextInt();
        io.grpc.Server server = ServerBuilder
                .forPort(port)
                .addService(new ChatService())
                .build();
        try {
            server.start();
            logger.info("Server started. Listening on port " + port);
        } catch (IOException e) {
            logger.error("Server error: ", e);
        }
    }

    public static void main(String[] args) {
        final Server server = new Server();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String message = scanner.nextLine();
            server.sendMessage(message);
        }
    }

    private class ChatService extends ChatGrpc.ChatImplBase {
        @Override
        public StreamObserver<Message> routeChat(final StreamObserver<Message> responseObserver) {
            userStream = responseObserver;

            return new StreamObserver<Message>() {
                public void onNext(Message message) {
                    logger.info("Message is received from client");
                    printMessage(message);
                }

                public void onError(Throwable t) {
                    logger.error("Client error:", t);
                }

                public void onCompleted() {
                    responseObserver.onCompleted();
                }
            };
        }
    }
}