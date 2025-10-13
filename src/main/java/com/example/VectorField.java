package com.example;

public abstract class VectorField {
    protected int cols;
    protected int rows;

    protected Grid2D curr;
    protected Grid2D prev;

    public void swap() {
        Grid2D temp = curr;
        curr = prev;
        prev = temp;
    }

    public Grid2D getCurr() {
        return curr;
    }

    public Grid2D getPrev() {
        return prev;
    }
}
