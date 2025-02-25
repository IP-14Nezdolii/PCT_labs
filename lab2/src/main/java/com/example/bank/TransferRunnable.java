package com.example.bank;

public class TransferRunnable implements Runnable {
    private static final int REPS = 1_000;

    private final Bank bank;
    private final int fromAccount;
    private final int maxAmount;

    public TransferRunnable(Bank b, int from, int max) {
        bank = b;
        fromAccount = from;
        maxAmount = max;
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < REPS; i++) {
                var toAccount = (int) (bank.accNumb() * Math.random());
                var amount = (int) (maxAmount * Math.random() / REPS);
    
                bank.transfer(fromAccount, toAccount, amount);
            }
        }   
    }
}
