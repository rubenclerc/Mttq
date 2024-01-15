package org.example;

public class Main {
    public static void main(String[] args) {

        MyMqttSubscriber subscriber = new MyMqttSubscriber();
        subscriber.createClient();
        subscriber.run();
    }
}