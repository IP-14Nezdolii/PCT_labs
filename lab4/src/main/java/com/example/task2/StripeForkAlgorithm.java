package com.example.task2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

import com.example.utils.Matrix;

public class StripeForkAlgorithm {

    public static Matrix multiply(Matrix first, Matrix second, int nThreads) {
        if (first.cols() != second.rows()) { 
            throw new IllegalArgumentException("Matrix dimensions are not compatible for multiplication");
        }
        if (nThreads < 1) {
            throw new IllegalArgumentException("Number of threads must be greater than 0");
        }

        Matrix result = null; 
        try (ForkJoinPool forkJoinPool = new ForkJoinPool(nThreads)) {
            result = forkJoinPool.invoke(new Multiply(first, second, nThreads));
        }

        return result;
    }

    private static class Multiply extends RecursiveTask<Matrix> {
        private final Matrix first;
        private final Matrix second;
        private final Matrix result;

        private final int N_TREADS;

        private final CyclicBarrier barrier;
        
                
        public Multiply(Matrix first, Matrix second, int nThreads) {
            this.first = first;
            this.second = second;
            this.result = new Matrix(first.rows(), second.cols());

            N_TREADS = Math.min(first.rows(), nThreads);
            barrier = new CyclicBarrier(N_TREADS);
        }


        @Override
        public Matrix compute() {
            List<RecursiveAction> actions = new ArrayList<>(N_TREADS);

            for (int i = 0; i < N_TREADS; i++) {
                var action = new StripeTask(i);
                action.fork();
                actions.add(action);
            }

            for (RecursiveAction action : actions) {
                action.join();
            }
            
            return result;
        }

        private class StripeTask extends RecursiveAction {
            private int rows = result.rows();
            private int cols = result.cols();
            private int startPos;

            StripeTask(int startPos) {
                this.startPos = startPos;
            }

            @Override
            protected void compute() {
                List<ForkJoinTask<Void>> actions = new ArrayList<>(N_TREADS);

                for (int j = startPos, count = 0; 
                    count < cols; 
                    j = (j + 1) % cols, count++
                ) {
                    for (int i = startPos; i < rows; i+= N_TREADS) {
                        var action = new CalcMatrixElem(i, j).fork();
                        actions.add(action);
                    }

                    try {
                        barrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    for (var action : actions) {
                        action.join();
                    }
                    actions.clear();
                }
            }
        }

        private class CalcMatrixElem extends RecursiveAction {
            private final int rowIndex; 
            private final int colIndex;

            CalcMatrixElem(int rowIndex, int colIndex) {
                this.rowIndex = rowIndex;
                this.colIndex = colIndex;
            }

            @Override
            protected void compute() {
                double sum = 0;
                for (int i = 0; i < first.cols(); i++) {
                    sum += first.get(rowIndex, i) * second.get(i, colIndex);
                }
                result.set(rowIndex, colIndex, sum);
            }
            
        }
    }
}
