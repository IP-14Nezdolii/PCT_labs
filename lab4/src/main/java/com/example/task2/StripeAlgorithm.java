package com.example.task2;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.example.utils.Matrix;

public class StripeAlgorithm {

    public static Matrix multiply(Matrix first, Matrix second, int nThreads) {
        if (first.cols() != second.rows()) { 
            throw new IllegalArgumentException("Matrix dimensions are not compatible for multiplication");
        }
        if (nThreads < 1) {
            throw new IllegalArgumentException("Number of threads must be greater than 0");
        }

        return (new Multiplyer(first, second, nThreads)).stripeMultiply();
    }

    private static class Multiplyer {
        private final Matrix first;
        private final Matrix second;
        private final Matrix result;

        private final CyclicBarrier barrier;
        private final Thread[] threads;
                
        public Multiplyer(Matrix first, Matrix second, int nThreads) {
            this.first = first;
            this.second = second;
            this.result = new Matrix(first.rows(), second.cols());

            nThreads = Math.min(first.rows(), nThreads);

            this.threads = new Thread[nThreads];
            barrier = new CyclicBarrier(threads.length);
        }

        public Matrix stripeMultiply() {

            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(createTask(i));
                threads[i].start();
            }

            try {
                for (Thread thread : threads) {
                    thread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            return result;
        }

        private Runnable createTask(int startPos) {
            return ()->{
                int nThreads = threads.length;

                int rows = result.rows();
                int cols = result.cols();

                for (int j = startPos, count = 0; 
                    count < cols; 
                    j = (j + 1) % cols, count++
                ) {
                    for (int i = startPos; i < rows; i+= nThreads) {
                        calcMatrixElem(i, j);
                    }

                    try {
                        barrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            };
        }

        private void calcMatrixElem(int rowIndex, int colIndex) {
            double sum = 0;
            for (int i = 0; i < first.cols(); i++) {
                sum += first.get(rowIndex, i) * second.get(i, colIndex);
            }
            result.set(rowIndex, colIndex, sum);
        }
    }
}
