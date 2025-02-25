package com.example.producerConsumer;

public class Producer implements Runnable {
    private Drop drop;
    
    private int[] arr;

    public Producer(Drop drop, int[] arr) {
        this.drop = drop;
        this.arr = arr;
    }

    public void run() {
        for (int i = 0; i < arr.length; i++)
            drop.put(arr[i]);
        drop.put(Integer.MIN_VALUE);
    }
}
