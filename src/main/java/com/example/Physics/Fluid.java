package com.example.Physics;

public class Fluid {
    public VelocityField vf;
    public SubstanceField sf;

    public Fluid(int cols, int rows) {
        this.vf = new VelocityField(cols, rows);
        this.sf = new SubstanceField(cols, rows, vf);
    }

    public void step(double dt) {
        vf.vStep(dt);
        sf.sStep(vf.vels, dt);
    }
}
