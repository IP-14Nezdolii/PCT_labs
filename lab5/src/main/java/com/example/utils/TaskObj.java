package com.example.utils;

import java.util.concurrent.locks.LockSupport;

public record TaskObj(
    long taskTimeMillis
) {
    public void doTask() {
        LockSupport.parkNanos(taskTimeMillis * 1_000_000L);
    }
}
