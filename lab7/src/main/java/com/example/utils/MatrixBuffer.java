package com.example.utils;

public class MatrixBuffer {
    Matrix[] data;
    int size = 0;

    public MatrixBuffer(int capacity) {
        data = new Matrix[capacity];
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    public void setData(Matrix[] data) {
        this.data = data;
        this.size = data.length;
    }

    public void add(Matrix matrix) {
        data[size] = matrix;
        size++;
    }

    public Matrix[] getData() {
       return this.data;
    }

    public int getSize() {
       return this.size;
    }
}
