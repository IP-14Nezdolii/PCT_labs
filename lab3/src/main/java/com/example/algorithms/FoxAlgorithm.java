package com.example.algorithms;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.example.utils.Matrix;
import com.example.utils.MatrixBlock;
import com.example.utils.Matrix.MatrixView;

public class FoxAlgorithm {

    public static Matrix multiply(Matrix first, Matrix second, int nThreads) {
        
        if (first.cols() != second.rows()) { 
            throw new IllegalArgumentException("Matrix dimensions are not compatible for multiplication");
        }
        if (nThreads < 1) {
            throw new IllegalArgumentException("Number of threads must be greater than 0");
        }

        int q = getQ(nThreads, first.rows(), second.cols());

        Matrix resizedFirst = getMatrixWithZeroes(first, q);
        Matrix resizedSecond = getMatrixWithZeroes(second, q);

        Matrix result = new Multiplyer(resizedFirst, resizedSecond, q, first.rows(), second.cols()).foxMultiply();
        return result;
    }

    private static int getQ(int nThreads, int width, int height) {
        int q = (int)Math.sqrt(nThreads);
        while ((q > width || q > height) && q > 1) {
            q--;
        }
        return q;
    }

    private static Matrix getMatrixWithZeroes(Matrix matrix, int q) {
        int k1 = q - (matrix.rows() % q);
        int k2 = q - (matrix.cols() % q);

        int rows = matrix.rows() + k1;
        int cols = matrix.cols() + k2;

        return rows == matrix.rows() && cols == matrix.cols() 
            ? matrix 
            : matrix.getView().getCopyMatrix(rows, cols);
    }

    private static class Multiplyer {
        private final MatrixView[][] first;
        private final MatrixView[][] second;

        private final int resultRows;
        private final int resultCols;
        private Matrix result;

        private final CyclicBarrier barrier;
        private final Thread[] threads;
        private final int q;

        public Multiplyer(
            Matrix first, 
            Matrix second, 
            int q,
            int resultRows,
            int resultCols
        ) {
            this.q = q;
            this.threads = new Thread[q*q];
            barrier = new CyclicBarrier(threads.length);

            this.first = getViews(first);
            this.second = getViews(second);

            this.resultRows = resultRows;
            this.resultCols = resultCols;

            this.result = new Matrix(first.rows(), second.cols());
        }

        private MatrixView[][] getViews(Matrix matrix) {
            int iStep = matrix.rows() / q;
            int jStep = matrix.cols() / q;

            MatrixView[][] views = new MatrixView[q][q];

            for (int i = 0; i < q; i++) {
                for (int j = 0; j < q; j++) {
                    views[i][j] = matrix.getView(i*iStep, j*jStep, iStep, jStep);
                }
            }

            return views;
        }

        public Matrix foxMultiply() {
            int iStep = result.rows() / q;
            int jStep = result.cols() / q;

            for (int i = 0; i < q; i++) {
                for (int j = 0; j < q; j++) {
                    threads[i * q + j] = new Thread(new FoxTask(
                        result.getView(i * iStep, j * jStep, iStep, jStep), 
                        i, 
                        j
                    )); 
                    threads[i * q + j].start();
                }
            }

            try {
                for (Thread thread : threads) {
                    thread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (resultRows != result.rows() || resultCols != result.cols()) {
                result = result.getView().getCopyMatrix(resultRows, resultCols);
            }
            return result;
        }

        private class FoxTask implements Runnable {
            private final MatrixView blockC;
            private final int iPos;
            private final int jPos;
            
            public FoxTask(MatrixView blockC, int iPos, int jPos) {
                this.blockC = blockC;
                this.iPos = iPos;
                this.jPos = jPos;
            }

            @Override
            public void run() {
                int i = iPos;
                
                for (int count = 0; count < q; count++) {
                    multiplyAndAdd(blockC, first[iPos][i], second[i][jPos]);

                    i = (i - 1 + q) % q;

                    try {
                        barrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }

            private void multiplyAndAdd(MatrixBlock dest, MatrixBlock a, MatrixBlock b) {  
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
    }
}
