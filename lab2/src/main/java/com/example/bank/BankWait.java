package com.example.bank;

public class BankWait extends Bank {
    private final Object[] lock;

    public BankWait(int n, int initialBalance) {
        super(n, initialBalance);

        lock = new Object[n];
        for (int i = 0; i < lock.length; i++) 
            lock[i] = new Object();
    }

    @Override
    synchronized public void transfer(int from, int to, int amount) {
        try {
            while (accounts[from] < amount)
                wait();

            accounts[from] -= amount;
            accounts[to] += amount;

            ntransacts++;
            if (ntransacts % NTEST == 0)
                test();
            
            notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
