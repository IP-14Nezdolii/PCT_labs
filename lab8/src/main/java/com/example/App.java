package com.example;

import com.example.utils.Matrix;

public class App {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 2; i <= 3; i++) {
            for (int j = 360; j <= 1440; j*=2) {
                run(i*i, j, 10);
            }
        }
    }

    public static void run(int nThreads, int matrixSize, int count) {
        final Matrix matrixA = Matrix.genRandomMatrix(matrixSize, matrixSize, 9);
        final Matrix matrixB = Matrix.genRandomMatrix(matrixSize, matrixSize, 10);

        var server = new Server();
        var client = new Client();

        server.setBaseMatricies(matrixA, matrixB);
        server.setTreadNumber(nThreads);

        client.setBaseMatricies(matrixA, matrixB);
        client.setCount(count);

        System.out.println("Matrix size: " + matrixSize);
        System.out.println("Threads: " + nThreads);
        System.out.println("Count: " + count);

        var tr1 = new Thread(server::start);
        var tr2 = new Thread(client::start);
        
        try {
            tr1.start();
            Thread.sleep(2000);
            tr2.start();

            tr1.join();
            tr2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
