package com.example.utils;

import java.util.Arrays;
import java.util.Random;

public class Matrix extends MatrixBlock {
    private final double[][] data;

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

    public double[][] getData() {
        return data;
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

    public static Matrix[] getBlocks(Matrix matrix, int n) {
        int q = (int) Math.sqrt(n);

        int iStep = matrix.rows() / q;
        int jStep = matrix.cols() / q;

        Matrix[] blocks = new Matrix[q * q];

        for (int i = 0; i < q; i++) {
            for (int j = 0; j < q; j++) {
                blocks[i * q + j] = matrix.getView(i*iStep, j*jStep, iStep, jStep).getCopyMatrix();
            }
        }

        return blocks;
    }

    public static Matrix blocksToMatrix(Matrix[] blocks) {
        int q = (int) Math.sqrt(blocks.length);

        Matrix result = new Matrix(blocks[0].rows() * q, blocks[0].cols() * q);

        for (int k = 0; k < blocks.length; k++) {
            Matrix block = blocks[k];

            int iPos = (k / q) * block.rows();
            int jPos = (k % q) * block.cols();

            for (int i = 0; i < block.rows(); i++) {
                for (int j = 0; j < block.cols(); j++) {
                    result.set(i + iPos, j + jPos, block.get(i, j));
                }
            }
        }

        return result;
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

        public Matrix getCopyMatrix(int copyRows, int copyCols) {
            Matrix result = new Matrix(copyRows, copyCols);

            copyRows = Math.min(result.rows(), rows());
            copyCols = Math.min(result.cols(), cols());

            for (int i = iPos, j = 0; i < copyRows + iPos; i++, j++) {
                // double[] src = matrix.data[i];
                // double[] dst = result.data[j];
                System.arraycopy(matrix.data[i], jPos, result.data[j], 0, copyCols);
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
