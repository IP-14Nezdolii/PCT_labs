package com.example.communicator;

import java.util.ArrayList;
import java.util.List;

import com.example.utils.Matrix;

import mpi.MPI;
import mpi.MPIException;
import mpi.Request;

public class OneToOneComm extends Communicator {
    private Matrix[] buff = new Matrix[2];
    private List<Request> requests;

    @Override
    public void initCommunicator(int rank, int nWorkers) {
        super.initCommunicator(rank, nWorkers);
        requests = new ArrayList<>(nWorkers);
    }

    @Override
	public Matrix[] blocksShareFromMaster(Matrix[] aMatrix, Matrix[] bMatrix) throws MPIException {
        int posState = getPosState();

		if (rank == MASTER_RANK) {
            for (Request request : requests) {
                request.Wait();
            }
            requests.clear();

            for (int workerRank = 1, i, j; workerRank < nWorkers; workerRank++) {
                i = (workerRank / q);
                j = (workerRank % q);

                buff[0] = aMatrix[toLinear(i, posState)];
                buff[1] = bMatrix[toLinear(posState, j)];

                requests.add(
                    MPI.COMM_WORLD.Isend(buff, 0, 2, MPI.OBJECT, workerRank, workerRank));
            }
            buff[0] = aMatrix[toLinear(0, posState)];
            buff[1] = bMatrix[toLinear(posState, 0)];
            
        } else {
            MPI.COMM_WORLD.Recv(buff, 0, 2, MPI.OBJECT, MASTER_RANK, rank);
        }

        changeIterationState();
        return buff;
	}

	@Override
	public Matrix[] blocksShareToMaster(Matrix matrix) throws MPIException {
		if (rank == MASTER_RANK) {
            Matrix[][] blocks = new Matrix[nWorkers][1]; 

            for (int i = 1; i < nWorkers; i++) {
                requests.add(
                    MPI.COMM_WORLD.Irecv(blocks[i], 0, 1, MPI.OBJECT, i, i));
            }
            blocks[0][0] = matrix;
      
            for (Request request : requests) {
                request.Wait();
            }
            requests.clear();
    
            Matrix[] result = new Matrix[nWorkers];
            for (int i = 0; i < blocks.length; i++) {
                result[i] = blocks[i][0];
            }
            return result;
        } else {
            buff[0] = matrix;
            MPI.COMM_WORLD.Isend(buff, 0, 1, MPI.OBJECT, MASTER_RANK, rank);
        }

		return null;
	}
}
