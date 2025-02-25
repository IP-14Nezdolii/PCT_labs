package com.example.bank;

public class Bank {
    protected static final int NTEST = 4_000_000;

    protected final int[] accounts;
    protected long ntransacts = 0;

    public Bank(int n, int initialBalance) {
        accounts = new int[n];

        for (int i = 0; i < accounts.length; i++)
            accounts[i] = initialBalance;
    }

    public void transfer(int from, int to, int amount) {
        accounts[from] -= amount;
        accounts[to] += amount;

        ntransacts++;
        if (ntransacts % NTEST == 0)
            test();
    }

    synchronized public void test() {
        int sum = 0;

        for (int i = 0; i < accounts.length; i++)
            sum += accounts[i];
            
        System.out.println("Transactions " + this.getClass().getSimpleName() + ": " + ntransacts + " Sum: " + sum);
    }

    public int accNumb() {
        return accounts.length;
    }
}