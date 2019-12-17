package com.kropiejohn.ftc.glasses.controller;

import com.kropiejohn.ftc.glasses.control.DoubleTextField;
import com.kropiejohn.ftc.glasses.control.IntegerTextField;
import com.kropiejohn.ftc.glasses.model.Glasses;
import com.kropiejohn.ftc.glasses.util.EnhancedNumberStringConverter;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

public class GlassesViewer {
    @FXML
    private DoubleTextField rightSphereInput;
    @FXML
    private DoubleTextField rightCylinderInput;
    @FXML
    private IntegerTextField rightAxisInput;
    @FXML
    private IntegerTextField rightFocalInput;
    @FXML
    private DoubleTextField leftSphereInput;
    @FXML
    private DoubleTextField leftCylinderInput;
    @FXML
    private IntegerTextField leftAxisInput;
    @FXML
    private IntegerTextField leftFocalInput;
    @FXML
    private TextField genderInput;
    @FXML
    private TextField ageInput;
    @FXML
    private TextField bifocalsInput;
    @FXML
    public TitledPane titlePane;

    private Glasses glasses;

    @FXML
    private void initialize() {
        glasses = new Glasses();

        // Right input setup.
        rightSphereInput.textProperty().bindBidirectional(glasses.rightSphereProperty(), new EnhancedNumberStringConverter());
        rightCylinderInput.textProperty().bindBidirectional(glasses.rightCylinderProperty(), new EnhancedNumberStringConverter());
        rightAxisInput.textProperty().bindBidirectional(glasses.rightAxisProperty(), new EnhancedNumberStringConverter());
        rightFocalInput.textProperty().bindBidirectional(glasses.rightFocalProperty(), new EnhancedNumberStringConverter());

        // Left input setup.
        leftSphereInput.textProperty().bindBidirectional(glasses.leftSphereProperty(), new EnhancedNumberStringConverter());
        leftCylinderInput.textProperty().bindBidirectional(glasses.leftCylinderProperty(), new EnhancedNumberStringConverter());
        leftAxisInput.textProperty().bindBidirectional(glasses.leftAxisProperty(), new EnhancedNumberStringConverter());
        leftFocalInput.textProperty().bindBidirectional(glasses.leftFocalProperty(), new EnhancedNumberStringConverter());

        // Misc. setup.
        genderInput.setText(glasses.getGender().getName());
        ageInput.setText(glasses.getAge().getName());
        bifocalsInput.setText(glasses.getBifocals().getName());
    }

    public void setGlasses(final Glasses newGlasses) {
        titlePane.setText("Inventory Number - " + newGlasses.getNumber());
        glasses.copy(newGlasses);

        genderInput.setText(newGlasses.getGender().getName());
        ageInput.setText(newGlasses.getAge().getName());
        bifocalsInput.setText(newGlasses.getBifocals().getName());
    }
}
