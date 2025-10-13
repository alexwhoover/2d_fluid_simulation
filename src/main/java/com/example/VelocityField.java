package com.example;

public class VelocityField extends VectorField {

    public VelocityField(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.curr = new Grid2D(cols, rows);
        this.prev = new Grid2D(cols, rows);
    }

    public void addForce(Grid2D F, double dt) {

    }

    public void advect(double dt) {

    }

    public void diffuse(double visc, double dt) {

    }

    public void project(double dt) {

    }
}
