package com.example.test;

public class Timer {
    private double start = 0;
    private long end = 0;

    public double start() {
        this.start = System.currentTimeMillis();
        return this.start;
    }

    public double end() {
        this.end = System.currentTimeMillis();
        return this.end;
    }

    public double resultTimeMillis() {
        return end - start;
    }

    public double resultTime() {
        return (end - start)/1_000.0;
    }
}
