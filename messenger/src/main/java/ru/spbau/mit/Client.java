package ru.spbau.mit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Scanner;


public class Client extends User{

    private static final Logger logger = LogManager.getLogger(Client.class);

    private Client() {
        logger.info("Creating client");

        Scanner scanner = new  Scanner(System.in);
        System.out.println("Enter your name: ");
        userName = scanner.nextLine();
        System.out.println("Enter host: ");
        String host = scanner.nextLine();
        System.out.println("Enter port: ");
        int port = scanner.nextInt();

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext(true)
                .build();
        ChatGrpc.ChatStub stub  = ChatGrpc.newStub(channel);
        userStream = stub.routeChat(new StreamObserver<Message>() {
            public void onNext(Message message) {
                logger.info("Message is received from " + host + " " + port);
                printMessage(message);
            }

            public void onError(Throwable throwable) {
                logger.error("Server error:", throwable);
            }

            public void onCompleted() {}
        });
    }

    public static void main(String[] args) {
        final Client client = new Client();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String message = scanner.nextLine();
            client.sendMessage(message);
        }
    }
}

