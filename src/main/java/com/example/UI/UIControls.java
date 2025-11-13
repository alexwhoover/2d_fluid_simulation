package com.example.UI;

import javafx.scene.control.ComboBox;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class UIControls {
    private VBox controlPanel;
    private InteractionMode intMode = InteractionMode.VELOCITY;
    private DisplayMode dispMode = DisplayMode.DENSITY;

    public UIControls() {
        controlPanel = new VBox();
        controlPanel.setPadding(new Insets(10));
        createControls();
    }

    public InteractionMode getInteractionMode() {
        return intMode;
    }

    public DisplayMode getDisplayMode() {
        return dispMode;
    }


    private void createControls() {
        // Interaction Mode ComboBox
        Label intModeLabel = new Label("Interaction Mode:");
        ComboBox<InteractionMode> intModeComboBox = new ComboBox<>();
        intModeComboBox.getItems().addAll(InteractionMode.values());
        intModeComboBox.setValue(InteractionMode.VELOCITY);
        intModeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            intMode = newVal;
        });
        controlPanel.getChildren().addAll(intModeLabel, intModeComboBox);

        // Display Mode ComboBox
        Label dispModeLabel = new Label("Display Mode:");
        ComboBox<DisplayMode> dispModeComboBox = new ComboBox<>();
        dispModeComboBox.getItems().addAll(DisplayMode.values());
        dispModeComboBox.setValue(DisplayMode.DENSITY);
        dispModeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            dispMode = newVal;
        });
        controlPanel.getChildren().addAll(dispModeLabel, dispModeComboBox);
    }

    public VBox getControlPanel() {
        return controlPanel;
    }
}
