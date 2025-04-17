package com.example.communicator;

import com.example.utils.Matrix;

import mpi.*;

public abstract class Communicator {
    protected Matrix[] buffer = new Matrix[2];

    public static final int MASTER_RANK = 0;

    protected int nWorkers;
    protected int rank;

    protected int counter = 0;
    protected int q;

    public void initCommunicator(int rank, int nWorkers) {
        this.rank = rank;
        this.nWorkers = nWorkers;

        this.q = (int) Math.sqrt(nWorkers);
    }

    public int getNWorkers() {
        return nWorkers;
    }

    public int getQ() {
        return q;
    }

    public int getRank() {
        return rank;
    }

    public int getCounter() {
        return counter;
    }

    abstract public void sendBlocksToWorkers(Matrix[] aMatrix, Matrix[] bMatrix) throws MPIException;
    abstract public Matrix[] receiveBlocksFromMaster() throws MPIException;

    abstract public void sendBlockToMaster(Matrix matrix) throws MPIException;
    abstract public Matrix[] receiveBlocksFromWorkers() throws MPIException;

    protected int toLinear(int iPos, int jPos) {
        return iPos * q + jPos;
    }

    protected int getPosByCounter(int iPos) {
        if (counter != 0) {
            for (int i = 0; i < counter; i++) {
                iPos = (iPos - 1 + q) % q;
            }
        }
        
        return iPos;
    }
}
