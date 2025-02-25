package com.example.symbols;

import java.util.Queue;
import java.util.LinkedList;

public class Symbols {
    private final int N_ROWS;
    private final int ROW_LENGTH = 10;
    private Queue<Runnable> queue = new LinkedList<Runnable>();

    private int rowCounter = 0;
    private int counter = 0;

    public Symbols(int rowNumb) {
        this.N_ROWS = rowNumb;
    }

    public void runSymbols() {
        for (Runnable runnable : queue) 
            new Thread(runnable).start();
    }

    public void addSymbol(char symbol) {
        System.out.println("Symbol added: " + symbol);
        queue.add(addSymbolTask(symbol, queue.size()));
    }

    private Runnable addSymbolTask(char symbol, int number) {
        return () -> {
            for (int j = 0; j < queue.size() * ROW_LENGTH * N_ROWS; j++)
                printSymbol(symbol, number);
        };
    }

    synchronized private void printSymbol(char symbol, int number) {
        try {
            while (number != counter)
                wait();

            System.out.print(symbol);

            counter++;
            rowCounter++;
            
            if (counter == queue.size()) {
                counter = 0;
                if (rowCounter == queue.size() * ROW_LENGTH) {
                    rowCounter = 0;
                    System.out.println();
                }
            }
            
            notifyAll();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
