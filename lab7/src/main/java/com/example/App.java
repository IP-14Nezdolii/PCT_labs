package com.example;

import com.example.communicator.ManyCommunicator;
import com.example.communicator.ManyToManyComm;
import com.example.communicator.ManyToOneComm;
import com.example.communicator.OneToManyComm;
import com.example.communicator.OneToOneComm;
import com.example.utils.Matrix;

import mpi.MPI;

public class App 
{
    private static int MATRIX_SIDE_SIZE = 1440;

    public static void main( String[] args )
    {
        MPI.Init(args);

        Matrix a = null;
        Matrix b = null;
        Matrix expected = null;

        if (MPI.COMM_WORLD.Rank() == 0) {
            a = Matrix.genRandomMatrix(
                MATRIX_SIDE_SIZE, MATRIX_SIDE_SIZE, 0);
            b = Matrix.genRandomMatrix(
                MATRIX_SIDE_SIZE, MATRIX_SIDE_SIZE, 2);

            double start = System.currentTimeMillis() / 1000.0;
            expected = Matrix.multiply(a,b);
            System.out.printf(
                "Single process total time: %-5.3f sec %n", 
                System.currentTimeMillis() / 1000.0 - start
            );
        }
        MPI.COMM_WORLD.Barrier();

        
        FoxMultiplyer.runMultiply(a, b, expected, new ManyToManyComm(), true);
        MPI.COMM_WORLD.Barrier();

        FoxMultiplyer.runMultiply(a, b, expected, new ManyToOneComm(), true);
        MPI.COMM_WORLD.Barrier();

        FoxMultiplyer.runMultiply(a, b, expected, new OneToManyComm(), true);
        MPI.COMM_WORLD.Barrier();

        FoxMultiplyer.runMultiply(a, b, expected, new OneToOneComm(), true);
        MPI.COMM_WORLD.Barrier();

        FoxMultiplyer.runMultiply(a, b, expected, new ManyCommunicator(), true);
        MPI.COMM_WORLD.Barrier();

        MPI.Finalize();
    }
}
