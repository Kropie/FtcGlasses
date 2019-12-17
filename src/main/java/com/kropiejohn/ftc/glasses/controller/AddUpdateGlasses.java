package com.kropiejohn.ftc.glasses.controller;

import com.kropiejohn.ftc.glasses.FtcGlasses;
import com.kropiejohn.ftc.glasses.model.*;
import com.kropiejohn.ftc.glasses.util.EnhancedNumberStringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

import java.util.Date;

public class AddUpdateGlasses {
    @FXML
    private Button addUpdateButton;

    @FXML
    private Label numberLabel;

    ///////////////////////
    // Right input fields.
    ///////////////////////
    @FXML
    private TextField rightSphereInput;
    @FXML
    private TextField rightCylinderInput;
    @FXML
    private TextField rightAxisInput;
    @FXML
    private TextField rightFocalInput;

    ///////////////////////
    // Left input fields.
    ///////////////////////
    @FXML
    private TextField leftSphereInput;
    @FXML
    private TextField leftCylinderInput;
    @FXML
    private TextField leftAxisInput;
    @FXML
    private TextField leftFocalInput;
    @FXML
    private ComboBox<Gender> genderInput;
    @FXML
    private ComboBox<Age> ageInput;
    @FXML
    private ComboBox<YesNo> bifocalsInput;

    private Glasses glasses;

    void setUpdateType(UpdateType updateType, int glassesNumber) {
        if (updateType == UpdateType.UPDATE) {
            glasses = GlassesDatabase.INSTANCE.get().get(glassesNumber);
        } else {
            glasses = new Glasses();
            glasses.setNumber(glassesNumber);
        }

        addUpdateButton.setText(updateType.getName());

        numberLabel.setText(String.valueOf(glasses.getNumber()));

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
        genderInput.getItems().addAll(Gender.values());
        genderInput.getSelectionModel().select(glasses.getGender());
        genderInput.getSelectionModel().selectedItemProperty().addListener((observableValue, oldGender, newGender) -> {
            glasses.setGender(newGender);
        });
        ageInput.getItems().addAll(Age.values());
        ageInput.getSelectionModel().select(glasses.getAge());
        ageInput.getSelectionModel().selectedItemProperty().addListener((observableValue, oldAge, newAge) -> {
            glasses.setAge(newAge);
        });
        bifocalsInput.getItems().addAll(YesNo.values());
        bifocalsInput.getSelectionModel().select(glasses.getBifocals());
        bifocalsInput.getSelectionModel().selectedItemProperty().addListener((observableValue, oldYesNo, newYesNo) -> {
            glasses.setBifocals(newYesNo);
        });
    }

    @FXML
    protected void addUpdateGlasses(final ActionEvent event) throws Exception {
        glasses.setEntryDate(new Date(System.currentTimeMillis()));
        GlassesDatabase.INSTANCE.get().put(glasses.getNumber(), glasses);
        FtcGlasses.goHome();
    }

    @FXML
    protected void cancel(final ActionEvent event) throws Exception {
        FtcGlasses.goHome();
    }
}
