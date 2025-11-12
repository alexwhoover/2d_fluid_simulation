package com.example;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;

public class FluidRenderer {
    private int cols;
    private int rows;
    private double cellSize;
    private VelocityField vf;

    public FluidRenderer(VelocityField vf, int cols, int rows, double sceneWidth, double sceneHeight) {
        this.cols = cols;
        this.rows = rows;
        this.vf = vf;

        double usableWidth = sceneWidth * 0.75;
        double usableHeight = sceneHeight * 0.90;

        // Calculate cellSize based on which dimension is the limiting factor
        double cellSizeByWidth = usableWidth / cols;
        double cellSizeByHeight = usableHeight / rows;

        // Use the smaller value to ensure grid fits
        this.cellSize = Math.min(cellSizeByWidth, cellSizeByHeight);
    }

    public VelocityField getVelocityField() {
        return vf;
    }

    public void vStep(Grid2D F, double visc, double dt) {
        vf.vStep(F, visc, dt);
    }

    public void drawPressure(Pane pane) {
        /*
        Grid is mirrored, so +y is down
         */
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double pressure = vf.getPressure(col, row);
                String pressureText = String.format("%.2f", pressure);

                // Calculate center position of the cell
                double centerX = col * cellSize + cellSize / 2;
                double centerY = row * cellSize + cellSize / 2;

                javafx.scene.text.Text text = new javafx.scene.text.Text(centerX, centerY, pressureText);
                text.setFill(Color.WHITE);
                text.setFont(javafx.scene.text.Font.font(16));

                // Center the text horizontally and vertically
                text.setTextOrigin(javafx.geometry.VPos.CENTER);
                text.setX(centerX - text.getLayoutBounds().getWidth() / 2);

                pane.getChildren().add(text);
            }
        }
    }

    public void drawVels(Pane pane) {
        /*
        Grid is mirrored, so +y is down
         */
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double speed = vf.getCurr().calcCentre(col, row);
                double norm = Math.min(speed, 10.0) / 10.0;

                Color cellColor = Color.gray(norm);
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
        cell.setStroke(Color.BLACK);
        cell.setStrokeWidth(1);
        pane.getChildren().add(cell);
    }

    public void drawArrows(Pane pane, double magnifier, Color color) {
        /*
        Grid is mirrored, so +y is down
         */
        double arrowLength = cellSize * magnifier;

        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                double cellX = i * cellSize;
                double cellY = j * cellSize;

                double leftVel = vf.getCurr().getLeft(i, j);
                drawArrow(pane, cellX, cellY + cellSize / 2, leftVel * arrowLength, 0, color);

                double rightVel = vf.getCurr().getRight(i, j);
                drawArrow(pane, cellX + cellSize, cellY + cellSize / 2, rightVel * arrowLength, 0, color);

                double topVel = vf.getCurr().getTop(i, j); // Bottom because mirrored
                drawArrow(pane, cellX + cellSize / 2, cellY + cellSize, 0, topVel * arrowLength, color);

                double bottomVel = vf.getCurr().getBottom(i, j); // Top because mirrored
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
