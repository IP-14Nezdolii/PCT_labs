package com.example.utils;

import java.util.Arrays;
import java.util.Random;

public class Matrix extends MatrixBlock {
    private double[][] data;

    public Matrix(int rows, int cols, double val) {
        super(rows, cols);

        data = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = val;
            }
        }
    }

    public Matrix(int rows, int cols) {
        super(rows, cols);
        data = new double[rows][cols];
    }

    @Override
    public void set(int i, int j, double value) {
        data[i][j] = value;
    }

    @Override
    public double get(int i, int j) {
        return data[i][j];
    }

    public double[] getRow(int i) {
        return data[i];
    }

    public double[] getColumn(int index) {
        return Arrays.stream(data).mapToDouble(doubles -> doubles[index]).toArray();
    }

    public static Matrix genRandomMatrix(int rows, int cols, long seed) {
        Random random = new Random(seed);

        Matrix matrix = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.data[i][j] = random.nextDouble();
            }
        }
        return matrix;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Matrix matrix = (Matrix) obj;
        return this.dataEquals(matrix);
    }

    public MatrixView getView() {
        return new MatrixView(0, 0, rows(), cols(), this);
    }
    public MatrixView getView(int iPos, int jPos, int rows, int cols) {
        return new MatrixView(iPos, jPos, rows, cols, this);
    }

    public class MatrixView extends MatrixBlock {
        private final int iPos; 
        private final int jPos;

        private final Matrix matrix;

        private MatrixView(
            int iPos, int jPos, 
            int rows, int cols,
            Matrix matrix
        ) {
            super(rows, cols);

            this.iPos = iPos;
            this.jPos = jPos;
            
            this.matrix = matrix;
        }

        public int getiPos() {
            return iPos;
        }

        public int getjPos() {
            return jPos;
        }

        @Override
        public double get(int i, int j) {
            return matrix.data[iPos + i][jPos+ j];
        }

        @Override
        public void set(int i, int j, double value) {
            matrix.data[iPos + i][jPos+ j] = value;
        }

        public Matrix getCopyMatrix() {
            return this.getCopyMatrix(rows(), cols());
        }

        public Matrix getCopyMatrix(int rows, int cols) {
            Matrix result = new Matrix(rows, cols);

            cols = Math.min(result.cols(), cols());
            rows = Math.min(result.rows(), rows());

            for (int i = iPos, j = 0; i < rows; i++, j++) {
                System.arraycopy(
                    matrix.data[i], 
                    0, 
                    result.data[j], 
                    0, 
                    cols
                );
            }

            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            MatrixView matrix = (MatrixView) obj;
            return this.dataEquals(matrix);
        }
    }

}
