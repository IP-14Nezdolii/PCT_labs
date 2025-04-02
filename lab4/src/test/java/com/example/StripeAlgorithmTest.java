package com.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.example.task2.StripeAlgorithm;
import com.example.utils.Matrix;

public class StripeAlgorithmTest 
{
    @Test
    public void testBasicMultiplication() {
        Matrix a = new Matrix(2, 2);
        a.set(0, 0, 1);
        a.set(0, 1, 2);
        a.set(1, 0, 3);
        a.set(1, 1, 4);

        Matrix b = new Matrix(2, 2);
        b.set(0, 0, 5);
        b.set(0, 1, 6);
        b.set(1, 0, 7);
        b.set(1, 1, 8);

        Matrix expected = Matrix.multiply(a, b);
        Matrix result = StripeAlgorithm.multiply(a, b, 2);

        assertEquals(expected, result);
    }

    @Test
    public void testSingleThreadMultiplication() {
        Matrix a = new Matrix(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                a.set(i, j, i + j);
            }
        }

        Matrix b = new Matrix(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                b.set(i, j, i - j);
            }
        }

        Matrix expected = Matrix.multiply(a, b);
        Matrix result = StripeAlgorithm.multiply(a, b, 1);

        assertEquals(expected, result);
    }

    @Test
    public void testMultipleThreadsMultiplication() {
        Matrix a = Matrix.genRandomMatrix(50, 100, 123);
        Matrix b = Matrix.genRandomMatrix(100, 60, 456);

        Matrix expected = Matrix.multiply(a, b);
        Matrix result = StripeAlgorithm.multiply(a, b, 4);

        assertEquals(expected, result);
    }

    @Test
    public void testSingleElementMatrix() {
        Matrix a = new Matrix(1, 1);
        a.set(0, 0, 5);

        Matrix b = new Matrix(1, 1);
        b.set(0, 0, 3);

        Matrix expected = new Matrix(1, 1);
        expected.set(0, 0, 15);

        Matrix result = StripeAlgorithm.multiply(a, b, 1);

        assertEquals(expected, result);
    }

    @Test
    public void testLargeMatrixWithExcessThreads() {
        Matrix a = Matrix.genRandomMatrix(3, 3, 789);
        Matrix b = Matrix.genRandomMatrix(3, 3, 101);

        Matrix expected = Matrix.multiply(a, b);
        Matrix result = StripeAlgorithm.multiply(a, b, 10);

        assertEquals(expected, result);
    }

    @Test
    public void testZeroMatrixMultiplication() {
        Matrix a = new Matrix(2, 2, 0);
        Matrix b = new Matrix(2, 2, 1);

        Matrix expected = new Matrix(2, 2, 0); 
        Matrix result = StripeAlgorithm.multiply(a, b, 2);

        assertEquals(expected, result);
    }
}
