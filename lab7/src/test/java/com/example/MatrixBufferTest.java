package com.example;

import org.junit.Test;

import com.example.utils.Matrix;
import com.example.utils.MatrixBuffer;

public class MatrixBufferTest {

    @Test
    public void matrixTest()
    {
        var buff = new MatrixBuffer(5);

        for (int i = 1; i < buff.getData().length + 1; i++) {
            buff.add(Matrix.genRandomMatrix(i, i, i));
        }

        for (var matrix : buff.getData()) {
            System.out.println(matrix);
        }
    }
}
