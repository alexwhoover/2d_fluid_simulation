package com.example;

public class Grid2D {
    /*
    Convention is +y is up
     */
    private double[][] vfx, vfy;
    public Grid2D(int cols, int rows) {
        this.vfx = new double[rows][cols + 1];
        this.vfy = new double[rows + 1][cols];
    }

    public double calcCentre(int i, int j) {
        double y = getTop(i, j) + getBottom(i, j);
        double x = getLeft(i, j) + getRight(i, j);
        return Math.abs(Math.sqrt(x*x + y*y));
    }

    public double getTop(int i, int j) {
        return vfy[j+1][i];
    }
    public double getBottom(int i, int j) {
        return vfy[j][i];
    }
    public double getLeft(int i, int j) {
        return vfx[j][i];
    }
    public double getRight(int i, int j) {
        return vfx[j][i+1];
    }

    public void setTop(int i, int j, double val) {
        vfy[j+1][i] = val;
    }
    public void setBottom(int i, int j, double val) {
        vfy[j][i] = val;
    }
    public void setLeft(int i, int j, double val) {
        vfx[j][i] = val;
    }
    public void setRight(int i, int j, double val) {
        vfx[j][i+1] = val;
    }

    public void randomize(double min, double max) {
        // Random horizontal velocities
        for (int row = 0; row < vfx.length; row++) {
            for (int col = 0; col < vfx[row].length; col++) {
                vfx[row][col] = Math.random() * (max - min) + min;
            }
        }

        // Random vertical velocities
        for (int row = 0; row < vfy.length; row++) {
            for (int col = 0; col < vfy[row].length; col++) {
                vfy[row][col] = Math.random() * (max - min) + min;
            }
        }
    }
}
