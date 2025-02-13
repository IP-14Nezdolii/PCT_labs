package com.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Counter {
    private int count  = 0;
    private final Object lockObj = new Object();

    private final Lock lock = new ReentrantLock();

    public synchronized void syncInc() {
        count++;
    }

    public synchronized void syncDec() {
        count--;
    }

    public void syncBlockInc() {
        synchronized(this) {
            count++;
        }
    }

    public synchronized void syncBlockDec() {
        synchronized(this) {
            count--;
        }
    }

    public void syncBlockLockInc() {
        synchronized(lockObj) {
            count++;
        }
    }

    public synchronized void syncBlockLockDec() {
        synchronized(lockObj) {
            count--;
        }
    }

    public void lockInc() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    public void lockDec() {
        lock.lock();
        try {
            count--;
        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        return count;
    }
}
