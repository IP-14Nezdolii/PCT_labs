package com.example.utils;

import java.io.Serializable;

public abstract class MatrixBlock implements Serializable {
    protected final int rows;
    protected final int cols;

    MatrixBlock(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    abstract public void set(int i, int j, double value);

    abstract public double get(int i, int j);

    public int rows() {
        return rows;
    }

    public int cols() {
        return cols;
    }

    public static Matrix multiply(MatrixBlock first, MatrixBlock second) {
        if (first.cols != second.rows) {
            throw new IllegalArgumentException("Matrix dimensions are not compatible for multiplication");
        }
        
        Matrix result = new Matrix(first.rows, second.cols);

        for (int i = 0; i < result.rows(); i++) {
            for (int j = 0; j < result.cols(); j++) {
                double sum = 0;
                for (int k = 0; k < first.cols; k++) {
                    sum += first.get(i, k) * second.get(k, j);
                }
                result.set(i, j, sum);
            }
        }
        return result;
    }

    public void add(MatrixBlock other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new IllegalArgumentException("Matrix dimensions are not compatible for addition");
        }

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.set(i, j, this.get(i, j) + other.get(i, j));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("[ ");
            for (int j = 0; j < cols; j++) {
                sb.append(String.format("%6.2f ", this.get(i,j)));
            }
            sb.append("]\n");
        }
        return sb.toString();
    }

    protected boolean dataEquals(MatrixBlock other) {
        if (this.rows() != other.rows() || this.cols() != other.cols()) {
            return false;
        }

        double epsilon = 1e-9;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (Math.abs(get(i, j) - other.get(i, j)) > epsilon){
                    return false;
                }
            }
        }

        return true;
    }
}
