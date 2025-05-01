package com.example;

import mpi.*;

//MPJExpress
public class App {
    final static int MASTER = 0;

    public static void main(String[] args) {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        final int n = 4;
        int rowsPerProc = n / size;

        if (n % size != 0) {
            if (rank == 0) {
                System.out.println(
                    "BAD!BAD! The number of processes must divide the number of rows evenly."
                );
            }
            MPI.Finalize();
            return;
        }

        int[][] arr1 = null; 
        int[][] arr2 = null;
        int[] result = new int[n]; 

        int[][] localA = new int[rowsPerProc][n];
        int[][] localB = new int[rowsPerProc][n];
        int[] localC = new int[rowsPerProc];

        if (rank == MASTER ) {
            arr1 = new int[n][n];
            arr2 = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    arr1[i][j] = i + j + 1;
                    arr2[i][j] = (i + 1) * (j + 1);
                }
            }
        }

        MPI.COMM_WORLD.Scatter(
            flatten2D(arr1), 0, rowsPerProc * n, MPI.INT,
            flatten2D(localA), 0, rowsPerProc * n, MPI.INT, 0
        );
        MPI.COMM_WORLD.Scatter(
            flatten2D(arr2), 0, rowsPerProc * n, MPI.INT,
            flatten2D(localB), 0, rowsPerProc * n, MPI.INT, 0
        );

        for (int i = 0; i < rowsPerProc; i++) {
            int sumA = 0, sumB = 0;
            for (int j = 0; j < n; j++) {
                sumA += localA[i][j];
                sumB += localB[i][j];
            }
            int avgA = sumA / n;
            int avgB = sumB / n;
            localC[i] = avgA * avgB;
        }

        MPI.COMM_WORLD.Gather(
            localC, 0, rowsPerProc, MPI.INT,
            result, 0, rowsPerProc, MPI.INT, 0
        );

        if (rank == MASTER ) {
            System.out.print("Arr C: ");
            for (int val : result) {
                System.out.print(val + " ");
            }
            System.out.println();
        }

        MPI.Finalize();
    }

    private static int[] flatten2D(int[][] matrix) {
        if (matrix == null) {
            return new int[0];
        }
        
        int rows = matrix.length;
        int cols = matrix[0].length;

        int[] flat = new int[rows * cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(matrix[i], 0, flat, i * cols, cols);
        }
        return flat;
    }
}