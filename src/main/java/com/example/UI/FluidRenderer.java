package com.example.UI;

import com.example.Physics.SubstanceField;
import com.example.Physics.VelocityField;
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

    public void drawVels(Pane pane, VelocityField vf) {
        /*
        Grid is mirrored, so +y is down
         */
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double speed = vf.vels.calcCentre(col, row);
                double norm = Math.min(speed, 20.0) / 20.0;

                Color cellColor = Color.color(norm, 0, 1-norm);
                fillCell(pane, row, col, cellColor);
            }
        }
    }

    public void drawDensity(Pane pane, SubstanceField sf) {
        /*
        Grid is mirrored, so +y is down
         */
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double density = sf.substance[row][col];
                double norm = Math.min(density, 2.0) / 2.0;

                Color cellColor = Color.color(norm, norm, norm);
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

    public void drawArrows(Pane pane, VelocityField vf, double magnifier, Color color) {
        /*
        Grid is mirrored, so +y is down
         */
        double arrowLength = cellSize * magnifier;

        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                double cellX = i * cellSize;
                double cellY = j * cellSize;

                double leftVel = vf.vels.getLeft(i, j);
                drawArrow(pane, cellX, cellY + cellSize / 2, leftVel * arrowLength, 0, color);

                double rightVel = vf.vels.getRight(i, j);
                drawArrow(pane, cellX + cellSize, cellY + cellSize / 2, rightVel * arrowLength, 0, color);

                double topVel = vf.vels.getTop(i, j); // Bottom because mirrored
                drawArrow(pane, cellX + cellSize / 2, cellY + cellSize, 0, topVel * arrowLength, color);

                double bottomVel = vf.vels.getBottom(i, j); // Top because mirrored
                drawArrow(pane, cellX + cellSize / 2, cellY, 0, bottomVel * arrowLength, color);
            }
        }
    }

    private void drawArrow(Pane pane, double x, double y, double dx, double dy, Color color) {
        Circle circle = new Circle(x, y, 3);
        circle.setStroke(color);
        circle.setFill(color);

        Line arrow = new Line(x, y, x + dx, y + dy);
        arrow.setStroke(color);
        arrow.setStrokeWidth(2.0);

        pane.getChildren().add(arrow);
        pane.getChildren().add(circle);
    }
}
