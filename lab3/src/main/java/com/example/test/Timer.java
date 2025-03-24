package com.example.test;

public class Timer {
    private double start = 0;
    private long end = 0;

    public double start() {
        this.start = System.nanoTime();
        return this.start;
    }

    public double end() {
        this.end = System.nanoTime();
        return this.end;
    }

    public double resultNanoTime() {
        return end - start;
    }

    public double resultMicroTime() {
        return (end - start)/1000.0;
    }
}
