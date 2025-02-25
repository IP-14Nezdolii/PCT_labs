package com.example.producerConsumer;

public class Consumer implements Runnable {
    private Drop drop;

    public Consumer(Drop drop) {
        this.drop = drop;
    }

    public void run() {
        for (int number = drop.take(); number != Integer.MIN_VALUE; number = drop.take())
            System.out.format("MESSAGE RECEIVED: %s%n", number);
    }
}
