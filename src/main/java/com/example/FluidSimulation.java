package com.example;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FluidSimulation extends Application
{
    private UIControls uicontrols;
    private Pane simCanvas;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) {
        setupUI();
        int cols = 10;
        int rows = 6;
        double cellSize = 1.0; // 1m is 1 grid width

        VelocityField vf = new VelocityField(cols, rows, cellSize);
        vf.getCurr().randomize(-3, 3);

        FluidRenderer renderer = new FluidRenderer(vf, cols, rows, scene.getWidth(), scene.getHeight());
        primaryStage.setTitle("Fluid Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();

        uicontrols.setVelocityField(vf);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                // Swap(U1, U0), Swap(S1, S0)
                // Vstep(U1, U0, visc, F, dt)
                // Sstep(S1, S0, kS, aS, U1, Ssource, dt)

                simCanvas.getChildren().clear();
                renderer.getVelocityField().solvePressure(uicontrols.getTimestep(), 50);
                renderer.drawVels(simCanvas);
                renderer.drawPressure(simCanvas);
                renderer.drawArrows(simCanvas, uicontrols.getArrowScale(), Color.SANDYBROWN);
            }
        };
        timer.start();
    }

    private void setupUI() {
        BorderPane root = new BorderPane();
        simCanvas = new Pane();
        uicontrols = new UIControls();

        StackPane centerContainer = new StackPane(simCanvas);
        StackPane.setMargin(simCanvas, new Insets(50));

        root.setCenter(centerContainer);
        root.setRight(uicontrols.getControlPanel());

        scene = new Scene(root, 1200, 800);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
