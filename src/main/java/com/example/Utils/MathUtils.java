package com.example.Utils;

import com.example.DataStructures.MACGrid;
import com.example.DataStructures.Vector;

public class MathUtils {
    public static Vector MAC_getValAtGridPos(MACGrid grid, Vector pos) {
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
        int jU1 = Math.min(jU + 1, grid.vfx.length - 1);
        int i1 = Math.min(i + 1, grid.vfx[0].length - 1);

        double u00 = grid.getX(i, jU);
        double u01 = grid.getX(i, jU1);
        double u10 = grid.getX(i1, jU);
        double u11 = grid.getX(i1, jU1);

        // Get v-component corners (vertical velocity)
        int iV = rightQuad ? i: i - 1;
        iV = Math.max(0, iV);
        int iV1 = Math.min(iV + 1, grid.vfy[0].length - 1);
        int j1 = Math.min(j + 1, grid.vfy.length - 1);

        double v00 = grid.getY(iV, j);
        double v01 = grid.getY(iV, j1);
        double v10 = grid.getY(iV1, j);
        double v11 = grid.getY(iV1, j1);

        // Finally, bilinearly interpolate
        double u = bilInterp(u00, u01, u10, u11, s, t);
        double v = bilInterp(v00, v01, v10, v11, s, t);

        return new Vector(u, v);
    }

    public static double bilInterp(double v00, double v01, double v10, double v11, double s, double t) {
        double vb = v00 + s * (v10 - v00);
        double vt = v01 + s * (v11 - v01);
        return vb + t * (vt - vb);
    }

    public static double calcCentre(MACGrid grid, int i, int j) {
        // Calculates magnitude of velocity vector (speed) at centre of cell
        double y = grid.getTop(i, j) + grid.getBottom(i, j);
        double x = grid.getLeft(i, j) + grid.getRight(i, j);
        return Math.abs(Math.sqrt(x*x + y*y));
    }

    public static double getValAtGridPos(double[][] grid, Vector pos, int cols, int rows) {
        // Clamp position to valid grid bounds (cell centers are at 0.5, 1.5, ..., cols-0.5)
        double x = Math.max(0.5, Math.min(cols - 0.5, pos.x));
        double y = Math.max(0.5, Math.min(rows - 0.5, pos.y));

        // Get the cell indices (subtract 0.5 because cell centers are offset)
        int i = (int)(x - 0.5);
        int j = (int)(y - 0.5);

        // Clamp to array bounds
        i = Math.max(0, Math.min(cols - 1, i));
        j = Math.max(0, Math.min(rows - 1, j));

        // Get fractional parts for interpolation
        double s = (x - 0.5) - i;
        double t = (y - 0.5) - j;

        // Get the four surrounding cell values
        double s00 = grid[j][i];
        double s10 = (i + 1 < cols) ? grid[j][i + 1] : s00;
        double s01 = (j + 1 < rows) ? grid[j + 1][i] : s00;
        double s11 = (i + 1 < cols && j + 1 < rows) ? grid[j + 1][i + 1] : s00;

        return bilInterp(s00, s01, s10, s11, s, t);
    }
}
