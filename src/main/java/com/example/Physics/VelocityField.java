package com.example.Physics;

import com.example.DataStructures.MACGrid;
import com.example.DataStructures.Vector;
import com.example.Utils.MathUtils;

public class VelocityField {
    /*
    Convention is +y is up
     */
    protected int cols;
    protected int rows;
    public MACGrid vels;

    private final double[][] pressureGrid; // Store pressure at each cell
    private final boolean[][] solidGrid; // Store which cells are solid (walls)

    private double density;

    public VelocityField(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.vels = new MACGrid(cols, rows);

        this.pressureGrid = new double[rows][cols];
        this.solidGrid = new boolean[rows][cols];
        addOuterWalls();
        addInnerCircle(10.0);
        this.density = 1.0;
    }

    public void vStep(double dt) {
        advect(dt);
        project(dt);
    }

    public void project(double dt) {
        solvePressure(dt, 100);
        double invK = dt / density;

        // Update horizontal velocities (on vertical boundaries)
        // There are (cols+1) vertical boundaries and rows cells vertically
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i <= cols; i++) {
                boolean leftSolid = isSolid(i - 1, j);
                boolean rightSolid = isSolid(i, j);

                if (leftSolid || rightSolid) {
                    vels.setX(i, j, 0.0);
                }
                else {
                    double u = vels.getX(i, j);
                    double pLeft = getPressure(i - 1, j);
                    double pRight = getPressure(i, j);
                    double v = u - invK * (pRight - pLeft);
                    vels.setX(i, j, v);
                }
            }
        }

        // Update vertical velocities (on horizontal boundaries)
        // There are cols cells horizontally and (rows+1) horizontal boundaries
        for (int j = 0; j <= rows; j++) {
            for (int i = 0; i < cols; i++) {
                boolean bottomSolid = isSolid(i, j - 1);
                boolean topSolid = isSolid(i, j);
                if (bottomSolid || topSolid) {
                    vels.setY(i, j, 0.0);
                }
                else {
                    double u = vels.getY(i, j);
                    double pBottom = getPressure(i, j - 1);
                    double pTop = getPressure(i, j);
                    double v = u - invK * (pTop - pBottom);
                    vels.setY(i, j, v);
                }
            }
        }
    }

    public void advect(double dt) {
        /*
        Semi-Lagrangian method.
        To solve for the advection part, we trace each point of
        the field backward in time. The new velocity at x is therefore
        the velocity that the particle had a time dt ago at the old location
        p(x; dt).
         */

        MACGrid vTemp = new MACGrid(cols, rows);

        // Update horizontal velocities (on vertical boundaries)
        // There are (cols+1) vertical boundaries and rows cells vertically
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i <= cols; i++) {
                boolean leftSolid = isSolid(i - 1, j);
                boolean rightSolid = isSolid(i, j);

                if (leftSolid || rightSolid) {
                    continue;
                }
                else {
                    Vector pos = new Vector(i, j + 0.5);
                    Vector vel = MathUtils.MAC_getValAtGridPos(vels, pos);
                    Vector posPrev = new Vector(pos.x - vel.x * dt, pos.y - vel.y * dt);
                    Vector velPrev = MathUtils.MAC_getValAtGridPos(vels, posPrev);
                    vTemp.setX(i, j, velPrev.x);
                }
            }
        }

        // Update vertical velocities (on horizontal boundaries)
        // There are cols cells horizontally and (rows+1) horizontal boundaries
        for (int j = 0; j <= rows; j++) {
            for (int i = 0; i < cols; i++) {
                boolean bottomSolid = isSolid(i, j - 1);
                boolean topSolid = isSolid(i, j);

                if (bottomSolid || topSolid) {
                    continue;
                }
                else {
                    Vector pos = new Vector(i + 0.5, j);
                    Vector vel = MathUtils.MAC_getValAtGridPos(vels, pos);
                    Vector posPrev = new Vector(pos.x - vel.x * dt, pos.y - vel.y * dt);
                    Vector velPrev = MathUtils.MAC_getValAtGridPos(vels, posPrev);
                    vTemp.setY(i, j, velPrev.y);
                }
            }
        }

        // Copy vTemp values to curr
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i <= cols; i++) {
                vels.setX(i, j, vTemp.getX(i, j));
            }
        }

        for (int j = 0; j <= rows; j++) {
            for (int i = 0; i < cols; i++) {
                vels.setY(i, j, vTemp.getY(i, j));
            }
        }
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
        if (isSolid(x, y)) {
            return;
        }

        double K = density / dt;

        int flowTop = isSolid(x, y + 1) ? 0 : 1;
        int flowBottom = isSolid(x, y - 1) ? 0 : 1;
        int flowLeft = isSolid(x - 1, y) ? 0 : 1;
        int flowRight = isSolid(x + 1, y) ? 0 : 1;
        int fluidEdgeCount = flowTop + flowBottom + flowLeft + flowRight;

        if (fluidEdgeCount == 0) {
            return;
        }

        double pTop = getPressure(x, y + 1) * flowTop;
        double pBottom = getPressure(x, y - 1) * flowBottom;
        double pLeft = getPressure(x - 1, y) * flowLeft;
        double pRight = getPressure(x + 1, y) * flowRight;

        double vTop = vels.getTop(x, y) * flowTop;
        double vBottom = vels.getBottom(x, y) * flowBottom;
        double vLeft = vels.getLeft(x, y) * flowLeft;
        double vRight = vels.getRight(x, y) * flowRight;

        double pSum = pTop + pBottom + pLeft + pRight;
        double dv = (vRight - vLeft) + (vTop - vBottom);
        pressureGrid[y][x] = (pSum - K * dv) / (double) fluidEdgeCount;
    }

    public boolean isSolid(int x, int y) {
        boolean outOfBounds = x < 0 || x >= cols || y < 0 || y >= rows;
        if (outOfBounds) {
            return true;
        }
        return solidGrid[y][x];
    }

    public void applyVelocityInRadius(double x, double y, double vx, double vy, double r) {
        /*
        x, y, and r are in grid coordinate system where 1 grid width = 1 unit distance
         */
        int minI = Math.max(0, (int) (x - r));
        int maxI = Math.min(cols, (int) (x + r) + 1);
        int minJ = Math.max(0, (int) (y - r));
        int maxJ = Math.min(rows, (int) (y + r) + 1);

        for (int j = minJ; j < maxJ; j++) {
            for (int i = minI; i < maxI; i++) {
                if (isSolid(i, j)) {
                    continue;
                }
                double dx = i - x;
                double dy = j - y;
                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist <= r) {
                    // Update u-velocity (on vertical cell boundaries)
                    if (i <= cols) {
                        double vx0 = vels.getX(i, j);
                        vels.setX(i, j, vx0 + vx);
                    }

                    // Update v-velocity (on horizontal cell boundaries)
                    if (j <= rows) {
                        double vy0 = vels.getY(i, j);
                        vels.setY(i, j, vy0 + vy);
                    }
                }
            }
        }
    }

    public void addOuterWalls() {
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                if (x == 0 || x == cols - 1 || y == 0 || y == rows - 1) {
                    solidGrid[y][x] = true;
                }
            }
        }
    }

    public void addInnerCircle(double radius) {
        double cx = cols / 2.0;
        double cy = rows / 2.0;

        int minX = (int) Math.max(0, cx - radius);
        int maxX = (int) Math.min(cx + radius, cols - 1);
        int minY = (int) Math.max(0, cy - radius);
        int maxY = (int) Math.min(cy + radius, rows - 1);

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                double dx =  x - cx;
                double dy = y - cy;
                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist <= radius) {
                    solidGrid[y][x] = true;
                }
            }
        }
    }
}
