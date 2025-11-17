package com.example.UI;

import com.example.DataStructures.Vector;
import com.example.Physics.Fluid;
import com.example.Physics.SubstanceField;
import com.example.Physics.VelocityField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class MouseInteraction {
    private final Fluid f;
    private final UIControls ui;

    private final double gridWidth; // Width of grid in screen distance
    private final double gridHeight; // Height of grid in screen distance
    private final int cols;
    private final int rows;

    private Vector lastPos;
    private boolean isDragging;

    private double timestep;
    private double radius;
    private double sAmount;
    private InteractionMode intMode;

    public MouseInteraction(Fluid f, UIControls ui, int cols, int rows, double gridWidth, double gridHeight, double timestep, double radius, double sAmount) {
        this.f = f;
        this.ui = ui;
        this.cols = cols;
        this.rows = rows;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.timestep = timestep;
        this.radius = radius;
        this.sAmount = sAmount;
        this.intMode = InteractionMode.VELOCITY;
        this.isDragging = false;
    }

    public void attachToPane(Pane pane) {
        pane.setOnMousePressed(event -> handleMousePressed(event));
        pane.setOnMouseDragged(event -> handleMouseDragged(event));
        pane.setOnMouseReleased(event -> handleMouseReleased(event));
    }

    private void handleMousePressed(MouseEvent event) {
        Vector screenPos = new Vector(event.getX(), event.getY());
        lastPos = screenToGrid(screenPos);
        isDragging = true;
    }

    private void handleMouseDragged(MouseEvent event) {
        if (!isDragging) {
            return;
        }

        intMode = ui.getInteractionMode();

        Vector screenPos = new Vector(event.getX(), event.getY());
        Vector currPos = screenToGrid(screenPos);

        switch (intMode) {
            case VELOCITY:
                Vector mouseVel = calcMouseVel(lastPos, currPos, 1.0);
                f.vf.applyVelocityInRadius(currPos.x, currPos.y, mouseVel.x, mouseVel.y, radius);
                break;

            case SUBSTANCE:
                f.sf.addSourceInRadius(currPos.x, currPos.y, sAmount, radius);
                break;
        }

        lastPos = currPos;
    }

    private void handleMouseReleased(MouseEvent event) {
        isDragging = false;
    }

    private Vector calcMouseVel(Vector lastPos, Vector currPos, double magnifier) {
        /*
        lastPos and currPos should be in grid coordinate system.
        Returns a velocity vector in units [1 gridcell width]/[s]
         */
        double vx = (currPos.x - lastPos.x) / timestep * magnifier;
        double vy = (currPos.y - lastPos.y) / timestep * magnifier;
        return new Vector(vx, vy);
    }

    public Vector screenToGrid(Vector screenCoord) {
        /*
        Convert a vector in screen coordinate system to grid coordinate system
         */
        double gridX = screenCoord.x * (cols / gridWidth);
        double gridY = screenCoord.y * (rows / gridHeight);
        return new Vector(gridX, gridY);
    }
}
