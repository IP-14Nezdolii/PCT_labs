package com.example;

import java.util.ArrayList;
import java.util.List;

import com.example.communicator.ManyCommunicator;
import com.example.communicator.ManyToManyComm;
import com.example.communicator.ManyToOneComm;
import com.example.communicator.OneToManyComm;
import com.example.communicator.OneToOneComm;
import com.example.utils.Matrix;

import mpi.MPI;

public class App 
{
    static List<Double> SingleTime = new ArrayList<>(10);
    static List<Double> OneToManyCommTime = new ArrayList<>(10);
    static List<Double> OneToOneCommTime = new ArrayList<>(10);
    static List<Double> ManyToManyCommTime = new ArrayList<>(10);
    static List<Double> ManyToOneCommTime = new ArrayList<>(10);
    static List<Double> ManyCommunicatorTime = new ArrayList<>(10);

    static final int COUNT = 10;

    public static void main( String[] args )
    {
        MPI.Init(args);

        run(1440,COUNT,0,false);

        run(360,COUNT,0,true);
        run(720,COUNT,0,true);
        run(1440,COUNT,0,true);

        MPI.Finalize();
    }

    static void test(
        int count, 
        Matrix a, 
        Matrix b, 
        Matrix expected
    ) {
        ManyToManyCommTime.clear();
        ManyToOneCommTime.clear();
        OneToManyCommTime.clear();
        OneToOneCommTime.clear();
        ManyCommunicatorTime.clear();

        for (int i = 0; i < count; i++) {
            MPI.COMM_WORLD.Barrier();
            ManyToManyCommTime.add(
                FoxMultiplyer.runMultiply(a, b, expected, new ManyToManyComm())
            );

            MPI.COMM_WORLD.Barrier();
            ManyToOneCommTime.add(
                FoxMultiplyer.runMultiply(a, b, expected, new ManyToOneComm())
            );

            MPI.COMM_WORLD.Barrier();
            OneToManyCommTime.add(
                FoxMultiplyer.runMultiply(a, b, expected, new OneToManyComm())
            );

            MPI.COMM_WORLD.Barrier();
            OneToOneCommTime.add(
                FoxMultiplyer.runMultiply(a, b, expected, new OneToOneComm())
            );

            MPI.COMM_WORLD.Barrier();
            ManyCommunicatorTime.add(
                FoxMultiplyer.runMultiply(a, b, expected, new ManyCommunicator())
            );
        }
    }

    static void outputTestResults(
        int sideSize, 
        int count, 
        boolean enableLog
    ) {
        if (MPI.COMM_WORLD.Rank() == 0) {
            if (enableLog) {
                System.out.println("OneToManyCommTime Avg: " 
                    + OneToManyCommTime.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
                System.out.println("OneToOneCommTime Avg: " 
                    + OneToOneCommTime.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
                System.out.println("ManyToManyCommTime Avg: " 
                    + ManyToManyCommTime.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
                System.out.println("ManyToOneCommTime Avg: " 
                    + ManyToOneCommTime.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
                System.out.println("ManyCommunicatorTime Avg: " 
                    + ManyCommunicatorTime.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
            }
        }
    }

    static void run(
        int sideSize, 
        int count, 
        int seed, 
        boolean enableLog
    ) {
        Matrix a = null;
        Matrix b = null;
        Matrix expected = null;

        if (MPI.COMM_WORLD.Rank() == 0) {
            a = Matrix.genRandomMatrix(
                sideSize, sideSize, seed);
            b = Matrix.genRandomMatrix(
                sideSize, sideSize, seed + 1);

            if (enableLog) {
                System.out.println("Matrix sideSize: " + sideSize);
            }
            
            SingleTime.clear();
            for (int index = 0; index < count; index++) {
                double start = System.currentTimeMillis() / 1000.0;
                expected = Matrix.multiply(a,b);
                double singleProcessTime = System.currentTimeMillis() / 1000.0 - start;

                SingleTime.add(singleProcessTime);
            }

            if (enableLog) {
                double averageSingleTime = SingleTime.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);

                System.out.printf(
                    "Single process average time: %-5.3f sec %n", 
                    averageSingleTime
                );
            }
        }
        test(count, a, b, expected);
        outputTestResults(sideSize, count, enableLog);
    } 
}