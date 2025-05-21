package com.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.example.utils.FoxAlgorithm;
import com.example.utils.Matrix;

public class FoxAlgorithmTest 
{
    @Test
    public void testBasicMultiplication() {
        Matrix a = Matrix.genRandomMatrix(2, 2, 9);
        Matrix b = Matrix.genRandomMatrix(2, 2, 10);

        Matrix expected = Matrix.multiply(a, b);
        Matrix result = FoxAlgorithm.multiply(a, b, 1);

        assertEquals(expected, result);
    }

    @Test
    public void testDiffMatrixMultiplication() {
        Matrix a = Matrix.genRandomMatrix(70, 40, 9);
        Matrix b = Matrix.genRandomMatrix(40, 90, 10);

        for (int nTreads = 4; nTreads <= 9; nTreads+=5) {

            Matrix expected = Matrix.multiply(a, b);
            Matrix result = FoxAlgorithm.multiply(a, b, nTreads);

            assertEquals(expected, result);
        }
    }

    @Test
    public void testSquareMatrixMultiplication() {
        Matrix a = Matrix.genRandomMatrix(100, 100, 5);
        Matrix b = Matrix.genRandomMatrix(100, 100, 111+5);

        for (int nTreads = 4; nTreads <= 9; nTreads+=5) {
            
            Matrix expected = Matrix.multiply(a, b);
            Matrix result = FoxAlgorithm.multiply(a, b, nTreads);

            assertEquals(expected, result);
        }
    }

    @Test
    public void testSingleElementMatrix() {
        Matrix a = new Matrix(1, 1);
        a.set(0, 0, 5);

        Matrix b = new Matrix(1, 1);
        b.set(0, 0, 3);

        Matrix expected = new Matrix(1, 1);
        expected.set(0, 0, 15);

        Matrix result = FoxAlgorithm.multiply(a, b, 1);

        assertEquals(expected, result);
    }

    @Test
    public void testLargeMatrixWithExcessThreads() {
        Matrix a = Matrix.genRandomMatrix(3, 3, 789);
        Matrix b = Matrix.genRandomMatrix(3, 3, 101);

        Matrix expected = Matrix.multiply(a, b);
        Matrix result = FoxAlgorithm.multiply(a, b, 10);

        assertEquals(expected, result);
    }

    @Test
    public void testZeroMatrixMultiplication() {
        Matrix a = new Matrix(2, 2, 0);
        Matrix b = new Matrix(2, 2, 1);

        Matrix expected = new Matrix(2, 2, 0); 
        Matrix result = FoxAlgorithm.multiply(a, b, 2);

        assertEquals(expected, result);
    }
}
