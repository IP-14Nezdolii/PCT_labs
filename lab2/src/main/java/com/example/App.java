package com.example;

import java.util.List;
import java.util.Random;

import com.example.bank.Bank;
import com.example.bank.BankAtomic;
import com.example.bank.BankLock;
import com.example.bank.BankWait;
import com.example.bank.TransferRunnable;
import com.example.journal.Grader;
import com.example.journal.Journal;
import com.example.producerConsumer.Consumer;
import com.example.producerConsumer.Drop;
import com.example.producerConsumer.Producer;
import com.example.symbols.Symbols;

public class App {

    public static void main(String[] args) {
        //runTask1(8, 10_000);
        runTask2(100);
        //runTask3(0);
        //runTask4();
    }

    static void runTask1(int nAccounts, int initialBalance) {
        bankRun(new Bank(nAccounts, initialBalance), initialBalance);
        bankRun(new BankAtomic(nAccounts, initialBalance), initialBalance);
        bankRun(new BankWait(nAccounts, initialBalance), initialBalance);
        bankRun(new BankLock(nAccounts, initialBalance), initialBalance);
    }

    static void bankRun(Bank b, int initialBalance) {
        for (int i = 0; i < b.accNumb(); i++) {
            Thread t = new Thread(new TransferRunnable(b, i, initialBalance));

            t.setPriority(Thread.NORM_PRIORITY + i % 2);
            t.start();
        }
    }

    static void runTask2(int size) {
        int[] array = new int[size];
        for (int i = 0; i < array.length; i++) 
            array[i] = i;

        Drop drop = new Drop();
        (new Thread(new Producer(drop, array))).start();
        (new Thread(new Consumer(drop))).start();
    }

    static void runTask3(int seed) {
        int N_WEEKS = 10;

        Journal journal = new Journal(3, 30);

        Grader.setNWeeks(N_WEEKS);
        Grader.setNMarks(6);
        

        List<Thread> threads = List.of(
            new Thread(new Grader(journal, new Random(seed + 1))),
            new Thread(new Grader(journal, new Random(seed + 2))),
            new Thread(new Grader(journal, new Random(seed + 3))),
            new Thread(new Grader(journal, new Random(seed + 4)))
        );

        threads.forEach(Thread::start);

        try {
            for (int i = 0; i < N_WEEKS; i++) {
                Thread.sleep(500);
                Grader.newWeek();
            }
    
            for (Thread thread : threads) 
                thread.join();
                
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        journal.outJournal();
    }

    static void runTask4() {
        var symbols = new Symbols(90);

        symbols.addSymbol('|');
        symbols.addSymbol('\\');
        symbols.addSymbol('/');

        symbols.runSymbols();
    }
}
