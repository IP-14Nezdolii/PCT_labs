package com.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.example.utils.Matrix;

/**
 * Unit test for simple App.
 */
public class MatrixTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void matrixTest()
    {
        Matrix matrix = null;
        Matrix result = null;

        Matrix[] blocks = null;
        
        matrix = Matrix.genRandomMatrix(4, 4, 0);
        blocks = Matrix.getBlocks(matrix, 4);
        result = Matrix.blocksToMatrix(blocks);
        assertEquals(matrix, result);

        matrix = Matrix.genRandomMatrix(16, 16, 0);
        blocks = Matrix.getBlocks(matrix, 4);
        result = Matrix.blocksToMatrix(blocks);
        assertEquals(matrix, result);

        matrix = Matrix.genRandomMatrix(8, 16, 0);
        blocks = Matrix.getBlocks(matrix, 4);
        result = Matrix.blocksToMatrix(blocks);
        assertEquals(matrix, result);

        matrix = Matrix.genRandomMatrix(16, 8, 0);
        blocks = Matrix.getBlocks(matrix, 4);
        result = Matrix.blocksToMatrix(blocks);
        assertEquals(matrix, result);

        matrix = Matrix.genRandomMatrix(9, 9, 0);
        blocks = Matrix.getBlocks(matrix, 9);
        result = Matrix.blocksToMatrix(blocks);
        assertEquals(matrix, result);
    }
}
