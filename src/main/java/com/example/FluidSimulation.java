package com.example;

import com.example.Physics.Fluid;
import com.example.Physics.SubstanceField;
import com.example.Physics.VelocityField;
import com.example.UI.*;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FluidSimulation extends Application
{
    private UIControls ui;
    private Pane simCanvas;
    private Scene scene;
    private final double dtPerFrame = 1.0 / 30.0;
    private final int cols = 100;
    private final int rows = 100 * 2/3;
    private final int sceneWidth = 1200;
    private final int sceneHeight = 800;
    private final double cellDist = 1.0;

    @Override
    public void start(Stage primaryStage) {
        setupUI();

        Fluid f = new Fluid(cols, rows);
        FluidRenderer renderer = new FluidRenderer(cols, rows, calcCellSize());
        MouseInteraction mouseInteraction = new MouseInteraction(f, ui, cols, rows, renderer.cellSize * cols, renderer.cellSize * rows, dtPerFrame, 3.0, 1.0);
        mouseInteraction.attachToPane(simCanvas);

        primaryStage.setTitle("Fluid Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
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
                        renderer.drawDensity(simCanvas, f.sf);
                        break;

                    case VELOCITY:
                        renderer.drawVels(simCanvas, f.vf);
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
