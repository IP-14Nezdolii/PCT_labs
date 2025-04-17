package com.example;

import com.example.communicator.BlockingComm;
import com.example.communicator.NonBlockingComm;

import mpi.MPI;

public class App 
{

    public static void main( String[] args )
    {
        MPI.Init(args);
        for (int i = 0; i < 10; i++) {
            FoxMultiplyer.runMultiply(new BlockingComm(), false);
            FoxMultiplyer.runMultiply(new NonBlockingComm(), false);
        }
        for (int i = 0; i < 10; i++) {
            FoxMultiplyer.runMultiply(new BlockingComm(), true);
            FoxMultiplyer.runMultiply(new NonBlockingComm(), true);
        }

        // FoxMultiplyer.runMultiply(new BlockingComm(), true);
        // FoxMultiplyer.runMultiply(new NonBlockingComm(), true);
        MPI.Finalize();
        
    }
}
