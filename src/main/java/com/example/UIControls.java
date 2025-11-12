package com.example;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class UIControls {
    private VBox controlPanel;

    // Temporary test
    private VelocityField vf;

    // Simulation parameters
    private double visc = 1.0; // mm^2/s
    private double timestep = 1.0/60.0; // s
    private double arrowScale = 0.1;

    public UIControls() {
        controlPanel = new VBox();
        controlPanel.setPadding(new Insets(10));
        createControls();
    }

    // Temporary test
    public void setVelocityField(VelocityField vf) {
        this.vf = vf;
    }

    private void createControls() {
        // Viscosity Slider
        Label viscLabel = new Label(String.format("Viscosity: %.2f mm^2/s", visc));
        Slider viscSlider = new Slider(1.0, 20.0, 1.0);
        viscSlider.setMajorTickUnit(0.5);
        viscSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            visc = newVal.doubleValue();
            viscLabel.setText(String.format("Viscosity: %.2f mm^2/s", visc));
        });

        // Add to VBox
        controlPanel.getChildren().addAll(viscLabel, viscSlider);

        // Timestep Slider
        Label timeLabel = new Label(String.format("Timestep: %.2f s", timestep));
        Slider timeSlider = new Slider(0.01, 1.0, 1.0/60.0);
        timeSlider.setMajorTickUnit(0.1);
        timeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            timestep = newVal.doubleValue();
            timeLabel.setText(String.format("Timestep: %.2f s", timestep));
        });

        // Add to VBox
        controlPanel.getChildren().addAll(timeLabel, timeSlider);

        // Timestep Slider
        Label arrowLabel = new Label(String.format("Arrow Scale: %.1f", arrowScale));
        Slider arrowSlider = new Slider(0, 0.4, 0.1);
        arrowSlider.setMajorTickUnit(0.1);
        arrowSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            arrowScale = newVal.doubleValue();
            arrowLabel.setText(String.format("Arrow Scale: %.1f", arrowScale));
        });

        // Add to VBox
        controlPanel.getChildren().addAll(arrowLabel, arrowSlider);

        Button pSolveBtn = new Button("Update Velocities");
        pSolveBtn.setOnAction(e -> {
            if (vf != null) {
                vf.updateVelocities(timestep);
            }
        });

        controlPanel.getChildren().add(pSolveBtn);
    }

    public VBox getControlPanel() {
        return controlPanel;
    }

    public double getVisc() {
        return visc;
    }

    public double getTimestep() {
        return timestep;
    }

    public double getArrowScale() {
        return arrowScale;
    }


}
