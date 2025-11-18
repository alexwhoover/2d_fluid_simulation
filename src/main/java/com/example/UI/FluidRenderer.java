package com.example.UI;

import com.example.Physics.Fluid;
import com.example.Physics.SubstanceField;
import com.example.Physics.VelocityField;
import com.example.Utils.MathUtils;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;

public class FluidRenderer {
    private int cols;
    private int rows;
    public double cellSize;

    public FluidRenderer(int cols, int rows, double cellSize) {
        this.cols = cols;
        this.rows = rows;
        this.cellSize = cellSize;
    }

    public void drawVels(Pane pane, Fluid f, double max) {
        /*
        Grid is mirrored, so +y is down
         */
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double speed = MathUtils.calcCentre(f.vf.vels, col, row);
                double norm = Math.min(speed, max) / max;

                Color cellColor = Color.color(norm, 0, 1-norm);
                fillCell(pane, row, col, cellColor);
            }
        }
    }

    public void drawDensity(Pane pane, Fluid f, double max) {
        /*
        Grid is mirrored, so +y is down
         */
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double density = f.sf.substance[row][col];
                double norm = Math.min(density, max) / max;
                Color cellColor;

                if (!f.vf.isSolid(col, row)) {
                    cellColor = Color.color(norm, norm, norm);
                }
                else {
                    cellColor = Color.DARKGRAY;
                }
                fillCell(pane, row, col, cellColor);
            }
        }
    }

    public void fillCell(Pane pane, int row, int col, Color color) {
        /*
        Grid is mirrored, so +y is down
         */
        Rectangle cell = new Rectangle(
                col * cellSize,
                row * cellSize,
                cellSize,
                cellSize
        );
        cell.setFill(color);
        cell.setStrokeWidth(1);
        pane.getChildren().add(cell);
    }
}
