package com.example.communicator;

import com.example.utils.Matrix;

import mpi.*;

public class BlockingComm extends Communicator {

    @Override
    public void sendBlocksToWorkers(Matrix[] aMatrix, Matrix[] bMatrix) throws MPIException {

        for (int i = 0; i < this.q; i++) {
            for (int j = 0; j < this.q; j++) {

                int workerRank = toLinear(i, j) + 1;

               //System.out.printf("Sending blocks #%d with worker rank #%d%n", counter, workerRank);

                buffer[0] = aMatrix[toLinear(i, getPosByCounter(i))];
                buffer[1] = bMatrix[toLinear(getPosByCounter(i), j)];

                MPI.COMM_WORLD.Send(
                    buffer, 0, 2, MPI.OBJECT, workerRank, workerRank);
            }
        }

        counter++;
    }

    @Override
    public Matrix[] receiveBlocksFromMaster() throws MPIException {
        //System.out.printf("Receiving blocks #%d, worker rank #%d%n", counter, rank);

        MPI.COMM_WORLD.Recv(buffer, 0, 2, MPI.OBJECT, MASTER_RANK, rank);

        //System.out.printf("Success receiving #%d, worker rank #%d%n", counter, rank);

        counter++;
        return buffer;
    }

    @Override
    public void sendBlockToMaster(Matrix matrix) throws MPIException {
        buffer[0] = matrix;
        MPI.COMM_WORLD.Send(
                    buffer, 0, 1, MPI.OBJECT, MASTER_RANK, rank);
    }

    @Override
    public Matrix[] receiveBlocksFromWorkers() throws MPIException {
        Matrix[] blocks = new Matrix[nWorkers];

        //System.out.printf("Receiving blocks master from  workers");

        for (int i = 1; i < nWorkers + 1; i++) {
            MPI.COMM_WORLD.Recv(buffer, 0, 1, MPI.OBJECT, i, i);

            blocks[i-1] = buffer[0];
        }

        return blocks;
    }

}
