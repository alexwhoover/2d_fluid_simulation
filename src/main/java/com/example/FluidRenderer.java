package com.example;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;

public class FluidRenderer {
    private int cols;
    private int rows;
    private double originX;
    private double originY;
    private double gridWidth;
    private double gridHeight;
    private double cellSize;

    public FluidRenderer(int cols, int rows, double sceneWidth, double sceneHeight) {
        this.cols = cols;
        this.rows = rows;

        double usableWidth = sceneWidth * 0.90;
        double usableHeight = sceneHeight * 0.90;

        // Calculate cellSize based on which dimension is the limiting factor
        double cellSizeByWidth = usableWidth / cols;
        double cellSizeByHeight = usableHeight / rows;

        // Use the smaller value to ensure grid fits
        this.cellSize = Math.min(cellSizeByWidth, cellSizeByHeight);

        // Calculate and save grid origin
        this.gridWidth = cols * cellSize;
        this.gridHeight = rows * cellSize;

        this.originX = (sceneWidth - gridWidth) / 2;
        this.originY = (sceneHeight - gridHeight) / 2;
    }

    public void drawVels(Pane pane, VelocityField u) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double speed = u.getCurr().calcCentre(col, row);
                double norm = Math.min(speed, 10.0) / 10.0;

                Color cellColor = Color.gray(norm);
                fillCell(pane, row, col, cellColor);
            }
        }
    }

    public void fillCell(Pane pane, int row, int col, Color color) {
        Rectangle cell = new Rectangle(
                originX + col * cellSize,
                originY + row * cellSize,
                cellSize,
                cellSize
        );
        cell.setFill(color);
        cell.setStroke(Color.BLACK);
        cell.setStrokeWidth(1);
        pane.getChildren().add(cell);
    }

    public void drawArrows(Pane pane, VelocityField u, double magnifier, Color color) {
        double arrowLength = cellSize * magnifier;

        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                // Top left of grid cell
                double cellX = originX + i * cellSize;
                double cellY = originY + j * cellSize;

                double leftVel = u.getCurr().getLeft(i, j);
                drawArrow(pane, cellX, cellY + cellSize / 2, 0, leftVel * arrowLength, color);

                double rightVel = u.getCurr().getRight(i, j);
                drawArrow(pane, cellX + cellSize, cellY + cellSize / 2, 0, rightVel * arrowLength, color);

                double topVel = u.getCurr().getTop(i, j);
                drawArrow(pane, cellX + cellSize / 2, cellY, topVel * arrowLength, 0, color);

                double bottomVel = u.getCurr().getBottom(i, j);
                drawArrow(pane, cellX + cellSize / 2, cellY + cellSize, bottomVel * arrowLength, 0, color);
            }
        }
    }
    
    private void drawArrow(Pane pane, double x, double y, double dx, double dy, Color color) {
        Circle circle = new Circle(x, y, 5);
        circle.setStroke(color);
        circle.setFill(color);

        Line arrow = new Line(x, y, x + dx, y + dy);
        arrow.setStroke(color);
        arrow.setStrokeWidth(3.0);

        // Calculate arrow tip
        double arrowLength = Math.sqrt(dx * dx + dy * dy);
        if (arrowLength > 10.0) {
            double tipSize = 10.0; // Size of the arrowhead

            // Normalize direction
            double normDx = dx / arrowLength;
            double normDy = dy / arrowLength;

            // Tip point
            double tipX = x + dx;
            double tipY = y + dy;

            // Calculate perpendicular vector for the arrowhead wings
            double perpX = -normDy;
            double perpY = normDx;

            // Create triangle arrowhead
            Polygon arrowHead = new Polygon(
                    tipX, tipY,  // Tip of arrow
                    tipX - normDx * tipSize + perpX * tipSize / 2,
                    tipY - normDy * tipSize + perpY * tipSize / 2,  // Left wing
                    tipX - normDx * tipSize - perpX * tipSize / 2,
                    tipY - normDy * tipSize - perpY * tipSize / 2   // Right wing
            );

            arrowHead.setFill(color);
            arrowHead.setStroke(color);

            pane.getChildren().add(arrowHead);
        }

        pane.getChildren().add(arrow);
        pane.getChildren().add(circle);
    }
}
