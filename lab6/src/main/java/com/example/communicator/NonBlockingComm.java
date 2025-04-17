package com.example.communicator;

import java.util.ArrayList;
import java.util.List;

import com.example.utils.Matrix;

import mpi.MPI;
import mpi.MPIException;
import mpi.Request;

public class NonBlockingComm extends Communicator{
    private List<Request> requests = new ArrayList<>(q * q);

    @Override
    public void sendBlocksToWorkers(Matrix[] aMatrix, Matrix[] bMatrix) throws MPIException {
        for (int i = 0; i < this.q; i++) {
            int iPos = getPosByCounter(i);
            for (int j = 0; j < this.q; j++) {

                int workerRank = toLinear(i, j) + 1;

                buffer[0] = aMatrix[toLinear(i, iPos)];
                buffer[1] = bMatrix[toLinear(iPos, j)];

                requests.add(
                    MPI.COMM_WORLD.Isend(buffer, 0, 2, MPI.OBJECT, workerRank, workerRank));
            }
        }

        for (Request request : requests) {
            request.Wait();
        }
        requests.clear();

        counter++;
    }

    @Override
    public Matrix[] receiveBlocksFromMaster() throws MPIException {
        MPI.COMM_WORLD.Irecv(buffer, 0, 2, MPI.OBJECT, MASTER_RANK, rank).Wait();
        counter++;
        return buffer;
    }

    @Override
    public void sendBlockToMaster(Matrix matrix) throws MPIException {
        buffer[0] = matrix;
        MPI.COMM_WORLD.Isend(
                    buffer, 0, 1, MPI.OBJECT, MASTER_RANK, rank).Wait();
    }

    @Override
    public Matrix[] receiveBlocksFromWorkers() throws MPIException {
        Matrix[][] blocks = new Matrix[nWorkers][1]; 

        for (int i = 1; i < nWorkers + 1; i++) {
            requests.add(
                MPI.COMM_WORLD.Irecv(blocks[i-1], 0, 1, MPI.OBJECT, i, i));
        }

        Matrix[] result = new Matrix[nWorkers];

        for (Request request : requests) {
            request.Wait();
        }
        requests.clear();

        for (int i = 0; i < blocks.length; i++) {
            result[i] = blocks[i][0];
        }

        return result;
    }
}
