package com.example.DataStructures;

import com.example.Utils.MathUtils;

public class MACGrid {
    /*
    Convention is +y is up
     */
    public double[][] vfx, vfy;
    public int rows, cols;
    public MACGrid(int cols, int rows) {
        this.vfx = new double[rows][cols + 1];
        this.vfy = new double[rows + 1][cols];
        this.rows = rows;
        this.cols = cols;
    }

    public double getX(int i, int j) { return getLeft(i,j); }
    public double getY(int i, int j) { return getBottom(i,j); }
    public double getTop(int i, int j) {
        return vfy[clampJ(j+1)][clampI(i)];
    }
    public double getBottom(int i, int j) {
        return vfy[clampJ(j)][clampI(i)];
    }
    public double getLeft(int i, int j) {
        return vfx[clampJ(j)][clampI(i)];
    }
    public double getRight(int i, int j) {
        return vfx[clampJ(j)][clampI(i+1)];
    }

    public void setX(int i, int j, double val) { setLeft(i, j, val); }
    public void setY(int i, int j, double val) { setBottom(i, j, val); }
    public void setTop(int i, int j, double val) { vfy[clampJ(j+1)][clampI(i)] = val; }
    public void setBottom(int i, int j, double val) { vfy[clampJ(j)][clampI(i)] = val; }
    public void setLeft(int i, int j, double val) { vfx[clampJ(j)][clampI(i)] = val; }
    public void setRight(int i, int j, double val) { vfx[clampJ(j)][clampI(i+1)] = val; }

    private int clampI(int i) {
        return clamp(i, 0, cols - 1);
    }

    private int clampJ(int j) {
        return clamp(j, 0, rows - 1);
    }

    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(val, max));
    }

    public MACGrid copyGrid() {
        MACGrid temp = new MACGrid(cols, rows);
        // Copy horizontal (vfx)
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i <= cols; i++) {
                temp.setX(i, j, this.getX(i, j));
            }
        }
        // Copy vertical (vfy)
        for (int j = 0; j <= rows; j++) {
            for (int i = 0; i < cols; i++) {
                temp.setY(i, j, this.getY(i, j));
            }
        }

        return temp;
    }

    public void pasteGrid(MACGrid other) {
        if (other == null) {
            throw new IllegalArgumentException("MACGrid cannot be null");
        }
        if (other.cols != this.cols || other.rows != this.rows) {
            throw new IllegalArgumentException("Grid dimensions do not match for pasteGrid.");
        }

        // Copy horizontal (vfx)
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i <= cols; i++) {
                this.setX(i, j, other.getX(i, j));
            }
        }
        // Copy vertical (vfy)
        for (int j = 0; j <= rows; j++) {
            for (int i = 0; i < cols; i++) {
                this.setY(i, j, other.getY(i, j));
            }
        }
    }
}
