package com.example.communicator;

import com.example.utils.Matrix;

import mpi.*;

public abstract class Communicator {
    public static final int MASTER_RANK = 0;

    protected int nWorkers;
    protected int rank;

    protected int counter = 0;
    protected int q;

    private int posState;

    public void initCommunicator(int rank, int nWorkers) throws IllegalArgumentException {
        this.rank = rank;
        this.nWorkers = nWorkers;

        this.q = (int) Math.sqrt(nWorkers);

        if (q * q != nWorkers) {
            throw new IllegalArgumentException("Number of workers must be a perfect square.");
        }

        posState = (rank / q);
    }

    abstract public Matrix[] blocksShareFromMaster(Matrix[] aMatrix, Matrix[] bMatrix) throws MPIException;
    abstract public Matrix[] blocksShareToMaster(Matrix matrix) throws MPIException;

    protected int toLinear(int i, int j) {
        return i * q + j;
    }

    protected void changeIterationState() {
        posState = (posState - 1 + q) % q;
        counter++;
    }

    protected int getPosState() { 
        return posState;
    }

    public int getNWorkers() {
       return this.nWorkers;
    }
 
    public int getRank() {
       return this.rank;
    }
 
    public int getCounter() {
       return this.counter;
    }

    public int getQ() {
       return this.q;
    }
}
