package com.example.bank;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class BankAtomic extends Bank {
    private final AtomicInteger[] accounts;

    public BankAtomic(int n, int initialBalance) {
        super(n, initialBalance);

        accounts = new AtomicInteger[n];
        for (int i = 0; i < n; i++) 
            accounts[i] = new AtomicInteger(initialBalance);
    }

    @Override
    public void transfer(int from, int to, int amount) {
        while (accounts[from].get() < amount) 
            LockSupport.parkNanos(800_000);
    
        if (accounts[from].get() < amount) 
            throw new IllegalStateException("Not enough money");

        accounts[to].addAndGet(-amount);
        accounts[from].addAndGet(amount);

        synchronized (this) {
            ntransacts++;
            if (ntransacts % NTEST == 0)
                test();
        }
    }

    @Override
    public void test() {
        int sum = 0;

        for (int i = 0; i < accounts.length; i++)
            sum += accounts[i].get();
            
        System.out.println(
            "Transactions " + 
            this.getClass().getSimpleName() + ": " + 
            ntransacts + 
            " Sum: " + sum);
    }

    @Override
    public int accNumb() {
        return accounts.length;
    }
}
