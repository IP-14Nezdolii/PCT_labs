package com.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.example.utils.FoxAlgorithm;
import com.example.utils.Matrix;

public class Server {
    private int nThreads = 4;

    private Matrix matrixA, matrixB;

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server started at 5000");
            handleClient(serverSocket.accept());
            
        } catch (IOException e) {
             e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) {
        try (
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ) {
            
            while (true) {
                String request = (String)ois.readObject();

                if ("END".equalsIgnoreCase(request)) {
                    break;

                } else if ("MUL".equalsIgnoreCase(request)) {
                    FoxAlgorithm.multiply(matrixA, matrixB, nThreads);

                    oos.writeObject("OK");
                    oos.flush();

                } else if ("SEND".equalsIgnoreCase(request)) {
                    FoxAlgorithm.multiply((Matrix)ois.readObject(), (Matrix)ois.readObject(), nThreads);

                    oos.writeObject("OK");
                    oos.flush();

                }else {
                    oos.writeObject("UNKNOWN REQUEST");
                    oos.flush();
                }
            } 
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public void setBaseMatricies(Matrix matrixA, Matrix matrixB) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
    }

    public void setTreadNumber(int nThreads) {
        this.nThreads = nThreads;
    }
}