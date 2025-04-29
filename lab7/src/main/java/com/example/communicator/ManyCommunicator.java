package com.example.communicator;

import com.example.utils.Matrix;
import com.example.utils.MatrixBuffer;

import mpi.MPI;
import mpi.MPIException;

public class ManyCommunicator extends Communicator {
    private MatrixBuffer sendBuffer;
    private MatrixBuffer getBuffer;

    @Override
    public void initCommunicator(int rank, int nWorkers) {
        super.initCommunicator(rank, nWorkers);
        
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
        sendBuffer.getData()[0] = matrix;
        getBuffer.clear();

        MPI.COMM_WORLD.Gather(
            sendBuffer.getData(), 0, 1, MPI.OBJECT, 
            getBuffer.getData(), 0, 1, MPI.OBJECT, MASTER_RANK
        );

		return getBuffer.getData();
	}
}
