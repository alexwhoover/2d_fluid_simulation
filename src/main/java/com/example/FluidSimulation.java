package com.example;

import javafx.scene.layout.Pane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Hello world!
 *
 */
public class FluidSimulation extends Application
{
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Fluid Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
