package com.example;

public class VelocityField extends VectorField {
    /*
    Convention is +y is up
     */
    private double pressureGrid[][];
    private double cellDist;
    private final double density;

    public VelocityField(int cols, int rows, double cellDist) {
        this.cols = cols;
        this.rows = rows;
        this.cellDist = 1.0;
        this.curr = new Grid2D(cols, rows);
        this.prev = new Grid2D(cols, rows);
        this.pressureGrid = new double[rows][cols];
        this.density = 1.0;
    }

    public double getPressure(int x, int y) {
        boolean outOfBounds = x < 0 || x >= cols || y < 0 || y >= rows;
        if (outOfBounds) {
            return 0.0;
        }
        return pressureGrid[y][x];
    }

    public void solvePressure(double dt, int iters) {
        for (int iter = 0; iter < iters; iter++) {
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++) {
                    pressureSolveCell(x, y, dt);
                }
            }
        }
    }

    public void pressureSolveCell(int x, int y, double dt) {
        double K = (density * cellDist) / dt;

        double pTop = getPressure(x, y + 1);
        double pBottom = getPressure(x, y - 1);
        double pLeft = getPressure(x - 1, y);
        double pRight = getPressure(x + 1, y);

        double vTop = curr.getTop(x, y);
        double vBottom = curr.getBottom(x, y);
        double vLeft = curr.getLeft(x, y);
        double vRight = curr.getRight(x, y);

        double pSum = pTop + pBottom + pLeft + pRight;
        double dv = (vRight - vLeft) + (vTop - vBottom);
        pressureGrid[y][x] = (pSum - K * dv) / 4.0;
    }

    public void updateVelocities(double dt) {
        double invK = dt / (density * cellDist);

        // Update horizontal velocities (on vertical boundaries)
        // There are (cols+1) vertical boundaries and rows cells vertically
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i <= cols; i++) {
                double u = curr.getLeft(i, j);
                double pRight = getPressure(i, j);
                double pLeft = getPressure(i - 1, j);
                double newU = u - invK * (pRight - pLeft);
                curr.setLeft(i, j, newU);
            }
        }

        // Update vertical velocities (on horizontal boundaries)
        // There are cols cells horizontally and (rows+1) horizontal boundaries
        for (int j = 0; j <= rows; j++) {
            for (int i = 0; i < cols; i++) {
                double v = curr.getBottom(i, j);
                double pTop = getPressure(i, j);
                double pBottom = getPressure(i, j - 1);
                double newV = v - invK * (pTop - pBottom);
                curr.setBottom(i, j, newV);
            }
        }
    }

    public void vStep(Grid2D F, double visc, double dt) {
//        solvePressure(dt);
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
