package com.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.example.utils.Matrix;

public class Client {
    private int count = 10;

    private Matrix matrixA, matrixB;

    public void start() {
        try (
            Socket socket = new Socket("localhost", 5000); 
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ) {
            System.out.println("Connected to server on port 5000");

            double startTime = 0;

            startTime = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                oos.writeObject("MUL");
                oos.flush();

                ois.readObject();
            }
            System.out.println((System.currentTimeMillis() - startTime) / count);

            startTime = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                oos.writeObject("SEND");
                oos.writeObject(matrixA);
                oos.writeObject(matrixB);
                oos.flush();

                ois.readObject();
            }
            System.out.println((System.currentTimeMillis() - startTime) / count);

            oos.writeObject("END");
        } catch (IOException | ClassNotFoundException e) {
             e.printStackTrace();
             Thread.currentThread().interrupt();
        }
    }

    public void setBaseMatricies(Matrix matrixA, Matrix matrixB) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
