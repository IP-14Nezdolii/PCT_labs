package com.example;

import com.example.communicator.Communicator;
import com.example.utils.Matrix;
import com.example.utils.MatrixBlock;

import mpi.MPI;
import mpi.MPIException;

public class FoxMultiplyer {
    public static double runMultiply(
        Matrix a, 
        Matrix b, 
        Matrix expected,
        Communicator comm
    ) throws MPIException {
        double totalTime = 0.0;
        comm.initCommunicator(MPI.COMM_WORLD.Rank(), MPI.COMM_WORLD.Size());

        if (comm.getRank() == Communicator.MASTER_RANK) { 
            double start = System.currentTimeMillis() / 1000.0;

            Matrix[] aBlocks = Matrix.getBlocks(a, comm.getNWorkers());
            Matrix[] bBlocks = Matrix.getBlocks(b, comm.getNWorkers());

            Matrix[] blocks = comm.blocksShareFromMaster(aBlocks, bBlocks);
            Matrix cBlock = new Matrix(blocks[0].rows(), blocks[1].cols());
            
            multiplyAndAdd(cBlock, blocks[0], blocks[1]);
            while (comm.getCounter() < comm.getQ()) {
                blocks = comm.blocksShareFromMaster(aBlocks, bBlocks);
                multiplyAndAdd(cBlock, blocks[0], blocks[1]);
            }
            Matrix c = Matrix.blocksToMatrix(
                comm.blocksShareToMaster(cBlock)
            );

            totalTime = System.currentTimeMillis() / 1000.0 - start;

            if (!c.equals(expected)) {
                throw new RuntimeException(
                    "BAD!BAD!BAD!, matrixSideSize: "
                    + a.rows()
                );
            }
        } else {
            Matrix[] blocks = comm.blocksShareFromMaster(null, null);
            Matrix cBlock = new Matrix(blocks[0].rows(), blocks[1].cols());

            multiplyAndAdd(cBlock, blocks[0], blocks[1]);
            while (comm.getCounter() < comm.getQ()) {
                blocks = comm.blocksShareFromMaster(null, null);
                multiplyAndAdd(cBlock, blocks[0], blocks[1]);
            }

            comm.blocksShareToMaster(cBlock);
        }

        return totalTime;
    }

    private static void multiplyAndAdd(MatrixBlock dest, MatrixBlock a, MatrixBlock b) {  
        for (int i = 0; i < a.rows(); i++) {
            for (int j = 0; j < b.cols(); j++) {
                double sum = 0;
                for (int k = 0; k < a.cols(); k++) {
                    sum += a.get(i, k) * b.get(k, j);
                }
                dest.set(i, j, dest.get(i, j) + sum);
            }
        }
    }
}