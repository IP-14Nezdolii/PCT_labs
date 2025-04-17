package com.example;

import com.example.communicator.Communicator;
import com.example.utils.Matrix;
import com.example.utils.MatrixBlock;

import mpi.MPI;
import mpi.MPIException;

public class FoxMultiplyer {
    private static final int MATRIX_SIDE_SIZE = 360;

    public static void runMultiply(Communicator comm, boolean enableLog) throws MPIException {
        comm.initCommunicator(MPI.COMM_WORLD.Rank(), MPI.COMM_WORLD.Size() - 1);

        if (comm.getRank() == Communicator.MASTER_RANK) {
            Matrix a = Matrix.genRandomMatrix(
                MATRIX_SIDE_SIZE, MATRIX_SIDE_SIZE, 0);
            Matrix b = Matrix.genRandomMatrix(
                MATRIX_SIDE_SIZE, MATRIX_SIDE_SIZE, 2);

            double start = System.currentTimeMillis() / 1000.0;

            Matrix[] aBlocks = Matrix.getBlocks(a, comm.getNWorkers());
            Matrix[] bBlocks = Matrix.getBlocks(b, comm.getNWorkers());
            
            while (comm.getCounter() < comm.getQ()){
                comm.sendBlocksToWorkers(aBlocks, bBlocks);
            }

            Matrix c = Matrix.blocksToMatrix(
                comm.receiveBlocksFromWorkers()
            );

            if (enableLog) {
                System.out.printf(
                    "Total time: %-5.3f sec %n", 
                    System.currentTimeMillis() / 1000.0 - start
                );
            }

            if (!c.equals(Matrix.multiply(a,b))) {
                throw new RuntimeException(
                    "BAD!BAD!BAD!, matrixSideSize: "
                    + MATRIX_SIDE_SIZE
                );
            } else if (enableLog) {
                System.out.println("true");
            }
            
        } else {
            Matrix[] blocks = comm.receiveBlocksFromMaster();
            Matrix cBlock = new Matrix(blocks[0].rows(), blocks[1].cols());

            multiplyAndAdd(cBlock, blocks[0], blocks[1]);

            while (comm.getCounter() < comm.getQ()) {
                blocks = comm.receiveBlocksFromMaster();
                multiplyAndAdd(cBlock, blocks[0], blocks[1]);
            }

            comm.sendBlockToMaster(cBlock);
        }
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