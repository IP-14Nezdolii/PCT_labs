package com.example.bank;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankLock extends Bank {
    private static final Lock lock = new ReentrantLock();
    private final Condition сondition = lock.newCondition();

    public BankLock(int n, int initialBalance) {
        super(n, initialBalance);
    }

    @Override
    public void transfer(int from, int to, int amount) {
        lock.lock();
        try {

            while (accounts[from] < amount) 
                сondition.await();
    
            accounts[from] -= amount;
            accounts[to] += amount;
            ntransacts++;
            if (ntransacts % NTEST == 0)
                test();
    
            сondition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
}
