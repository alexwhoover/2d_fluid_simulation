package com.example.DataStructures;

public class MACGrid {
    /*
    Convention is +y is up
     */
    private double[][] vfx, vfy;
    public MACGrid(int cols, int rows) {
        this.vfx = new double[rows][cols + 1];
        this.vfy = new double[rows + 1][cols];
    }

    public double calcCentre(int i, int j) {
        // Calculates magnitude of velocity vector (speed) at centre of cell
        double y = getTop(i, j) + getBottom(i, j);
        double x = getLeft(i, j) + getRight(i, j);
        return Math.abs(Math.sqrt(x*x + y*y));
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

    public Vector getValAtGridPos(Vector pos) {
        /*
        Returns the bilinearly interpolated value v(x, y), where x, y are grid coordinates.
        Horizontal values are stored at (1.5i, j)
        Vertical values are stored at (i, 1.5j)
         */

        // Lower left quadrant
        // s <= 0.5, t <= 0.5
        // x velocity, u, [(i,j-1), (i,j), (i+1,j), (i+1,j-1)]
        // y velocity, v, [(i-1,j), (i-1,j+1), (i,j+1), (i,j)]

        //  Upper left quadrant
        // s <= 0.5, t > 0.5
        // x velocity, u, [(i,j), (i,j+1), (i+1,j+1), (i+1,j)]
        // y velocity, v, [(i-1,j), (i-1,j+1), (i,j+1), (i,j)]

        // Upper right quadrant
        // s > 0.5, t > 0.5
        // x velocity, u, [(i,j), (i,j+1), (i+1,j+1), (i+1,j)]
        // y velocity, v, [(i,j), (i,j+1), (i+1,j+1), (i+1,j)]

        // Lower right quadrant
        // s > 0.5, t <= 0.5
        // x velocity, u, [(i,j-1), (i,j), (i+1,j), (i+1,j-1)]
        // y velocity, v, [(i,j), (i,j+1), (i+1,j+1), (i+1,j)]

        int i = (int) pos.x;
        int j = (int) pos.y;

        double s = pos.x - i;
        double t = pos.y - j;

        // Determine quadrant
        boolean rightQuad = s > 0.5;
        boolean upperQuad = t > 0.5;

        // Get u-component corners (horizontal vel)
        int jU = upperQuad ? j: j - 1;
        jU = Math.max(0, jU); // OOB check
        int jU1 = Math.min(jU + 1, vfx.length - 1);
        int i1 = Math.min(i + 1, vfx[0].length - 1);

        double u00 = getX(i, jU);
        double u01 = getX(i, jU1);
        double u10 = getX(i1, jU);
        double u11 = getX(i1, jU1);

        // Get v-component corners (vertical velocity)
        int iV = rightQuad ? i: i - 1;
        iV = Math.max(0, iV);
        int iV1 = Math.min(iV + 1, vfy[0].length - 1);
        int j1 = Math.min(j + 1, vfy.length - 1);

        double v00 = getY(iV, j);
        double v01 = getY(iV, j1);
        double v10 = getY(iV1, j);
        double v11 = getY(iV1, j1);

        // Finally, bilinearly interpolate
        double u = getValAtGridPos(u00, u01, u10, u11, s, t);
        double v = getValAtGridPos(v00, v01, v10, v11, s, t);

        return new Vector(u, v);
    }

    public double getValAtGridPos(double v00, double v01, double v10, double v11, double s, double t) {
        double vb = v00 + s * (v10 - v00);
        double vt = v01 + s * (v11 - v01);
        return vb + t * (vt - vb);
    }
}
