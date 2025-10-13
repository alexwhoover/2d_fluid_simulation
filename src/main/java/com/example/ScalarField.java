package com.example;

public class ScalarField extends VectorField
{
    public ScalarField(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.curr = new Grid2D(cols, rows);
        this.prev = new Grid2D(cols, rows);
    }

    public void addForce(Grid2D Source, double dt) {

    }

    public void advect(Grid2D U, double dt) {

    }

    public void diffuse(double kS, double dt) {

    }

    public void dissipate(double aS, double dt) {

    }
}
