package com.example;

import com.example.utils.Matrix;

public class App {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 2; i <= 3; i++) {
            for (int j = 360; j <= 1440; j*=2) {
                runV1(i*i, j, 10, false);
            }
        }

        System.out.println("Version 1");
        for (int i = 2; i <= 3; i++) {
            for (int j = 360; j <= 1440; j*=2) {
                runV1(i*i, j, 10, true);
            }
        }

        System.out.println("Version 2");
        for (int i = 2; i <= 3; i++) {
            for (int j = 360; j <= 1440; j*=2) {
                runV2(i*i, j, 10, true);
            }
        }
    }

    public static void runV1(int nThreads, int matrixSize, int count, boolean enableLogging) {
        final Matrix matrixA = Matrix.genRandomMatrix(matrixSize, matrixSize, 9);
        final Matrix matrixB = Matrix.genRandomMatrix(matrixSize, matrixSize, 10);

        var server = new Server();
        var client = new Client(enableLogging);

        server.setBaseMatricies(matrixA, matrixB);
        server.setTreadNumber(nThreads);

        client.setBaseMatricies(matrixA, matrixB);
        client.setCount(count);

        if (enableLogging) {
            System.out.println("Matrix size: " + matrixSize+ " Threads: " + nThreads);
        }

        var tr1 = new Thread(server::start);
        var tr2 = new Thread(client::startV1);
        
        try {
            tr1.start();
            Thread.sleep(2000);
            tr2.start();

            tr1.join();
            tr2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public static void runV2(int nThreads, int matrixSize, int count, boolean enableLogging) {
        final Matrix matrixA = Matrix.genRandomMatrix(matrixSize, matrixSize, 9);
        final Matrix matrixB = Matrix.genRandomMatrix(matrixSize, matrixSize, 10);

        var server = new Server();
        var client = new Client(enableLogging);

        server.setBaseMatricies(matrixA, matrixB);
        server.setTreadNumber(nThreads);

        client.setBaseMatricies(matrixA, matrixB);
        client.setCount(count);

        if (enableLogging) {
            System.out.println("Matrix size: " + matrixSize+ " Threads: " + nThreads);
        }

        var tr1 = new Thread(server::start);
        var tr2 = new Thread(client::startV2);
        
        try {
            tr1.start();
            Thread.sleep(2000);
            tr2.start();

            tr1.join();
            tr2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
