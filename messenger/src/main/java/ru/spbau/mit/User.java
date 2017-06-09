package ru.spbau.mit;

import io.grpc.stub.StreamObserver;

import java.text.SimpleDateFormat;
import java.util.Date;

abstract class User {
    protected StreamObserver<Message> userStream = null;
    protected String userName = null;
    protected void sendMessage(String message) {
        Message msg = Message
                .newBuilder()
                .setName(userName)
                .setTime(new SimpleDateFormat("dd.MM hh:mma").format(new Date()))
                .setContent(message)
                .build();
        if (userStream != null) {
            userStream.onNext(msg);
        }
    }

    protected void printMessage(Message message) {
        System.out.println(message.getTime() + " "
                + message.getName() + ": "
                + message.getContent());
    }
}