package com.example;

import com.example.Physics.Fluid;
import com.example.UI.*;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FluidSimulation extends Application
{
    private UIControls ui;
    private Pane simCanvas;
    private Scene scene;
    private final double dtPerFrame = 1.0 / 60.0;
    private final int cols = 100;
    private final int rows = 100 * 2/3;
    private final int sceneWidth = 1200;
    private final int sceneHeight = 800;

    @Override
    public void start(Stage primaryStage) {
        /*
        Fluid Object: Stores the discretized fluid representation, updates values on timesteps
        MouseInteraction Object: Handles click-and-drag interactions with fluid
        FluidRenderer Object: Handles drawing the fluid to the screen
         */
        setupUI();
        FluidRenderer renderer = new FluidRenderer(cols, rows, calcCellSize());
        Fluid f = new Fluid(cols, rows);
        MouseInteraction mouseInteraction = new MouseInteraction(f, ui, cols, rows, calcCellSize() * cols, calcCellSize() * rows, dtPerFrame, 3.0, 1.0);
        mouseInteraction.attachToPane(simCanvas);
        primaryStage.setTitle("Fluid Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            /*
            ApplicationTimer updates the screen in accordance with refresh rate of monitor,
            usually around 60Hz. However, I want the velocity and substance values to only
            update at a specified timestep, leading to the code below.
             */
            private long prevTime = 0;
            private double acc = 0.0;

            @Override
            public void handle(long now) {
                if (prevTime == 0) {
                    prevTime = now;
                    return;
                }

                double deltaTime = (now - prevTime) / 1_000_000_000.0;
                prevTime = now;
                acc += deltaTime;

                // Read current values from UI each frame
                DisplayMode dispMode = ui.getDisplayMode();

                while (acc >= dtPerFrame) {
                    f.step(dtPerFrame);
                    acc -= dtPerFrame;
                }

                // Update Canvas
                simCanvas.getChildren().clear();
                switch(dispMode) {
                    case DENSITY:
                        renderer.drawDensity(simCanvas, f.sf, 2.0);
                        break;

                    case VELOCITY:
                        renderer.drawVels(simCanvas, f.vf, 20.0);
                        break;
                }
            }
        };
        timer.start();
    }

    private void setupUI() {
        simCanvas = new Pane();
        simCanvas.setPrefSize(sceneWidth, sceneHeight);

        ui = new UIControls();
        VBox cp = ui.getControlPanel();
        cp.setMaxWidth(VBox.USE_PREF_SIZE);

        StackPane root = new StackPane(simCanvas, cp);
        StackPane.setAlignment(cp, Pos.CENTER_RIGHT);

        scene = new Scene(root, sceneWidth, sceneHeight);
    }

    public double calcCellSize() {
        double cellSizeByWidth = (double) sceneWidth / cols;
        double cellSizeByHeight = (double) sceneHeight / rows;
        return Math.min(cellSizeByWidth, cellSizeByHeight);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
