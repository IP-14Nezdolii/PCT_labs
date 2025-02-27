package com.example.bank;

public class BankSync extends Bank {

    public BankSync(int n, int initialBalance) {
        super(n, initialBalance);
    }

    @Override
    public void transfer(int from, int to, int amount) {
        try {
            synchronized (accounts) {
                while (accounts[from] < amount)
                    accounts.wait();

                accounts[from] -= amount;
                accounts[to] += amount;
    
                ntransacts++;
                if (ntransacts % NTEST == 0)
                    test();
                
                accounts.notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
