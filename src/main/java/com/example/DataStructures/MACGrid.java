package com.example.DataStructures;

import com.example.Utils.MathUtils;

public class MACGrid {
    /*
    Convention is +y is up
     */
    public double[][] vfx, vfy;
    public MACGrid(int cols, int rows) {
        this.vfx = new double[rows][cols + 1];
        this.vfy = new double[rows + 1][cols];
    }

    public double getX(int i, int j) { return getLeft(i,j); }
    public double getY(int i, int j) { return getBottom(i,j); }
    public double getTop(int i, int j) { return vfy[j+1][i]; }
    public double getBottom(int i, int j) { return vfy[j][i]; }
    public double getLeft(int i, int j) { return vfx[j][i]; }
    public double getRight(int i, int j) { return vfx[j][i+1]; }

    public void setX(int i, int j, double val) { setLeft(i, j, val); }
    public void setY(int i, int j, double val) { setBottom(i, j, val); }
    public void setTop(int i, int j, double val) { vfy[j+1][i] = val; }
    public void setBottom(int i, int j, double val) { vfy[j][i] = val; }
    public void setLeft(int i, int j, double val) { vfx[j][i] = val; }
    public void setRight(int i, int j, double val) { vfx[j][i+1] = val; }
}
