package com.example.communicator;

import java.util.ArrayList;
import java.util.List;

import com.example.utils.Matrix;
import com.example.utils.MatrixBuffer;

import mpi.MPI;
import mpi.MPIException;
import mpi.Request;

public class OneToManyComm extends Communicator {
    private MatrixBuffer sendBuffer;
    private MatrixBuffer getBuffer;
    private List<Request> requests;

    @Override
    public void initCommunicator(int rank, int nWorkers) {
        super.initCommunicator(rank, nWorkers);
        
        requests = new ArrayList<>(nWorkers);
        sendBuffer = new MatrixBuffer(nWorkers * 2);
        getBuffer = new MatrixBuffer(nWorkers);
    }

    @Override
	public Matrix[] blocksShareFromMaster(Matrix[] aMatrix, Matrix[] bMatrix) throws MPIException {
		if (rank == MASTER_RANK) {
            for (int i = 0; i < this.q; i++) {
                int iPos = getPosState();
                for (int j = 0; j < this.q; j++) {
                    sendBuffer.add(aMatrix[toLinear(i, iPos)]);
                    sendBuffer.add(bMatrix[toLinear(iPos, j)]);
                }
            }

        } 
        
        MPI.COMM_WORLD.Scatter(
            sendBuffer.getData(), 0, 2, MPI.OBJECT, 
            getBuffer.getData(), rank * 2, 2, MPI.OBJECT, MASTER_RANK
        );
        sendBuffer.clear();

        changeIterationState();
        return getBuffer.getData();
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
            sendBuffer.getData()[0] = matrix;
            MPI.COMM_WORLD.Isend(sendBuffer.getData(), 0, 1, MPI.OBJECT, MASTER_RANK, rank);
        }

		return null;
	}
}
