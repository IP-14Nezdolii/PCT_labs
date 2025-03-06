package com.example.bench;

public class Timer {
    private long start = 0;
    private long end = 0;

    public long start() {
        this.start = System.currentTimeMillis();
        return this.start;
    }

    public long end() {
        this.end = System.currentTimeMillis();
        return this.end;
    }

    public float resultTimeMillis() {
        return end - start;
    }

    public float resultTime() {
        return ((float)(end - start))/1_000;
    }
}
