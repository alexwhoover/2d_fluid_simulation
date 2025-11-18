package com.example.UI;

import com.example.DataStructures.Vector;
import com.example.Physics.Fluid;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class MouseInteraction {
    private final Fluid f;
    private final UIControls uicontrols;

    private final double cellSize;

    private Vector lastPos;
    private boolean isDragging;

    private double dt;
    private double radius;
    private double sAmount;
    private InteractionMode intMode;

    public MouseInteraction(Fluid f, UIControls uicontrols, double cellSize, double dt, double radius, double sAmount) {
        this.f = f;
        this.uicontrols = uicontrols;
        this.cellSize = cellSize;
        this.dt = dt;
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
        lastPos = clampGridCoord(screenToGrid(screenPos));
        isDragging = true;
    }

    private void handleMouseDragged(MouseEvent event) {
        if (!isDragging) {
            return;
        }

        intMode = uicontrols.getInteractionMode();

        Vector screenPos = new Vector(event.getX(), event.getY());
        Vector currPos = clampGridCoord(screenToGrid(screenPos));

        switch (intMode) {
            case VELOCITY:
                Vector mouseVel = calcMouseVel(lastPos, currPos, 3.0);
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
        double vx = (currPos.x - lastPos.x) / dt * magnifier;
        double vy = (currPos.y - lastPos.y) / dt * magnifier;
        return new Vector(vx, vy);
    }

    public Vector screenToGrid(Vector screenCoord) {
        /*
        Convert a vector in screen coordinate system to grid coordinate system
         */
        double gridX = screenCoord.x / cellSize;
        double gridY = screenCoord.y / cellSize;
        return new Vector(gridX, gridY);
    }

    private Vector clampGridCoord(Vector gridCoord) {
        double clampedI = Math.max(0, Math.min(gridCoord.x, cellSize * f.cols - 1));
        double clampedJ = Math.max(0, Math.min(gridCoord.y, cellSize * f.rows - 1));
        return new Vector(clampedI, clampedJ);
    }
}
