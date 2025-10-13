package com.example;

import javafx.scene.layout.Pane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FluidSimulation extends Application
{
    @Override
    public void start(Stage primaryStage) {
        // JavaFX Scene
        Pane root = new Pane();
        Scene scene = new Scene(root, 1200, 800);

        // FluidRenderer Class responsible for drawing to screen
        FluidRenderer renderer = new FluidRenderer(5, 3, scene.getWidth(), scene.getHeight());

        // Create random VelocityField to test
        VelocityField u = new VelocityField(5, 3);
        u.getCurr().randomize(-3, 3);

        renderer.drawVels(root, u);
        renderer.drawArrows(root, u, 0.1, Color.DARKORANGE);


        primaryStage.setTitle("Fluid Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
