package com.example.Physics;

import com.example.DataStructures.MACGrid;
import com.example.DataStructures.Vector;
import com.example.Utils.MathUtils;

public class SubstanceField
{
    /*
    A difference between ScalarField and VelocityField is that
    current and previous values are stored at grid centres, not edges.
     */
    public double[][] substance;
    private int cols, rows;
    private double cellDist;
    private VelocityField vf;

    public SubstanceField(int cols, int rows, VelocityField vf) {
        this.cols = cols;
        this.rows = rows;
        this.substance = new double[rows][cols];
        this.vf = vf;
    }

    public void sStep(MACGrid U, double dt) {
        advect(U, dt);
    }

    public void advect(MACGrid U, double dt) {
        /*
        Semi-Lagrangian method.
        To solve for the advection part, we trace each point of
        the field backward in time. The new velocity at x is therefore
        the velocity that the particle had a time dt ago at the old location
        p(x; dt).
         */

        double[][] sTemp = new double[rows][cols];

        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                boolean solid = vf.isSolid(i, j);

                if (solid) {
                    continue;
                }
                else {
                    Vector pos = new Vector(i + 0.5, j + 0.5);
                    Vector vel = MathUtils.MAC_getValAtGridPos(U, pos);
                    Vector posPrev = new Vector(pos.x - vel.x * dt, pos.y - vel.y * dt);
                    double subDensity = MathUtils.getValAtGridPos(substance, posPrev, cols, rows);
                    sTemp[j][i] = subDensity;
                }
            }
        }

        // Copy sTemp values to substance
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                substance[j][i] = sTemp[j][i];
            }
        }
    }

    public void addSourceInRadius(double gridX, double gridY, double amount, double radius) {
        int minI = Math.max(0, (int)(gridX - radius));
        int maxI = Math.min(cols - 1, (int)(gridX + radius));
        int minJ = Math.max(0, (int)(gridY - radius));
        int maxJ = Math.min(rows - 1, (int)(gridY + radius));

        for (int j = minJ; j <= maxJ; j++) {
            for (int i = minI; i <= maxI; i++) {
                double dx = i - gridX;
                double dy = j - gridY;
                double dist = Math.sqrt(dx*dx + dy*dy);

                if (dist <= radius) {
                    substance[j][i] += amount;
                }
            }
        }
    }
}
