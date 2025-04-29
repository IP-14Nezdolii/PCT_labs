package com.example.communicator;

import java.util.ArrayList;
import java.util.List;

import com.example.utils.Matrix;
import com.example.utils.MatrixBuffer;

import mpi.MPI;
import mpi.MPIException;
import mpi.Request;

public class ManyToOneComm extends Communicator {
    private Matrix[] buff = new Matrix[2];
    private MatrixBuffer getBuffer;
    private List<Request> requests;

    @Override
    public void initCommunicator(int rank, int nWorkers) {
        super.initCommunicator(rank, nWorkers);
        
        requests = new ArrayList<>(nWorkers);
        getBuffer = new MatrixBuffer(nWorkers);
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
        buff[0] = matrix;

        MPI.COMM_WORLD.Gather(
            buff, 0, 1, MPI.OBJECT, 
            getBuffer.getData(), 0, 1, MPI.OBJECT, MASTER_RANK
        );

		return getBuffer.getData();
	}
}
