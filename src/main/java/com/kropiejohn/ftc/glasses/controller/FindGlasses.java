package com.kropiejohn.ftc.glasses.controller;

import com.kropiejohn.ftc.glasses.FtcGlasses;
import com.kropiejohn.ftc.glasses.control.DoubleTextField;
import com.kropiejohn.ftc.glasses.control.IntegerTextField;
import com.kropiejohn.ftc.glasses.model.*;
import com.kropiejohn.ftc.glasses.util.EnhancedNumberStringConverter;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller used to find glasses given prescription information.
 */
public class FindGlasses {
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
    ///////////////////////
    // Glasses model.
    ///////////////////////
    private Glasses glasses;

    ///////////////////////
    // Search parameters.
    ///////////////////////
    @FXML
    private DoubleTextField sphereRangeInput;
    private static double DEFAULT_SPHERE_RANGE = 0.25;
    private DoubleProperty sphereRange;
    @FXML
    private DoubleTextField cylinderRangeInput;
    private static double DEFAULT_CYLINDER_RANGE = 0.5;
    private DoubleProperty cylinderRange;
    @FXML
    private IntegerTextField axisRangeInput;
    private static int DEFAULT_AXIS_RANGE = 10;
    private IntegerProperty axisRange;
    @FXML
    private IntegerTextField focalRangeInput;
    private static int DEFAULT_FOCAL_RANGE = 0;
    private IntegerProperty focalRange;

    ///////////////////////
    // Results.
    ///////////////////////
    @FXML
    public Tab resultsTab;
    @FXML
    private VBox resultsBox;
    @FXML
    public ScrollPane resultsScrollPane;
    @FXML
    public Button printButton;

    private List<Glasses> foundGlasses = new ArrayList<>();
    private static final int GLASSES_PER_PAGE = 4;
    private static final Font PRINTED_FONT = Font.font("Monospaced", 12.0);

    @FXML
    private void initialize() {
        ////////////////////////////////
        // Initialize the glasses model.
        ////////////////////////////////
        glasses = new Glasses();
        glasses.setNumber(-1);

        ////////////////////////////////////
        // Initialize prescription controls.
        ////////////////////////////////////
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

        ////////////////////////////////////////
        // Initialize search parameter controls.
        ////////////////////////////////////////
        sphereRange = new SimpleDoubleProperty(DEFAULT_SPHERE_RANGE);
        sphereRangeInput.textProperty().bindBidirectional(sphereRange, new EnhancedNumberStringConverter());
        cylinderRange = new SimpleDoubleProperty(DEFAULT_CYLINDER_RANGE);
        cylinderRangeInput.textProperty().bindBidirectional(cylinderRange, new EnhancedNumberStringConverter());
        axisRange = new SimpleIntegerProperty(DEFAULT_AXIS_RANGE);
        axisRangeInput.textProperty().bindBidirectional(axisRange, new EnhancedNumberStringConverter());
        focalRange = new SimpleIntegerProperty(DEFAULT_FOCAL_RANGE);
        focalRangeInput.textProperty().bindBidirectional(focalRange, new EnhancedNumberStringConverter());

        ////////////////////////////////////////
        // Initialize search results.
        ////////////////////////////////////////
        resultsScrollPane.setFitToHeight(true);
        resultsBox.getChildren().remove(printButton);
    }

    @FXML
    protected void cancel(final ActionEvent event) throws Exception {
        FtcGlasses.goHome();
    }

    public void findGlasses(ActionEvent event) {
        // Clear old results.
        resultsBox.getChildren().clear();
        foundGlasses.clear();

        GlassesDatabase.INSTANCE.getAll().forEach((value) -> {
            // Check right side.
            var validGlasses = inRange(glasses.getRightSphere(), value.getRightSphere(), sphereRange.get());
            validGlasses &= inRange(glasses.getRightCylinder(), value.getRightCylinder(), cylinderRange.get());
            validGlasses &= inRange(glasses.getRightAxis(), value.getRightAxis(), axisRange.get());
            validGlasses &= inRange(glasses.getRightFocal(), value.getRightFocal(), focalRange.get());
            // Check left side.
            validGlasses &= inRange(glasses.getLeftSphere(), value.getLeftSphere(), sphereRange.get());
            validGlasses &= inRange(glasses.getLeftCylinder(), value.getLeftCylinder(), cylinderRange.get());
            validGlasses &= inRange(glasses.getLeftAxis(), value.getLeftAxis(), axisRange.get());
            validGlasses &= inRange(glasses.getLeftFocal(), value.getLeftFocal(), focalRange.get());
            // Check other data.
            validGlasses &= isGenderValid(glasses.getGender(), value.getGender());
            validGlasses &= Objects.equals(glasses.getAge(), value.getAge());
            validGlasses &= !value.isRemoved();

            if (validGlasses) {
                try {
                    addResults(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        if (foundGlasses.isEmpty()) {
            alert.setContentText("Unable to find any glasses.");
        } else {
            alert.setContentText("Found " + foundGlasses.size() + " glasses.");
        }
        alert.showAndWait();
    }

    private boolean inRange(Number targetNumber, Number numberToTest, Number plusMinusRange) {
        final var floor = targetNumber.doubleValue() - plusMinusRange.doubleValue();
        final var ceiling = targetNumber.doubleValue() + plusMinusRange.doubleValue();

        return numberToTest.doubleValue() >= floor && numberToTest.doubleValue() <= ceiling;
    }

    private boolean isGenderValid(final Gender targetGender, final Gender genderToTest) {
        return Objects.equals(targetGender, genderToTest)
                || targetGender == Gender.UNISEX
                || genderToTest == Gender.UNISEX;
    }

    public void resetSearch(ActionEvent event) {
        glasses.reset();
        // Clear results.
        resultsBox.getChildren().clear();
        foundGlasses.clear();
    }

    private void addResults(final Glasses newGlasses) throws Exception {
        if (!resultsBox.getChildren().contains(printButton)) {
            resultsBox.getChildren().add(printButton);
            printButton.setVisible(true);
        }

        var loader = new FXMLLoader(FtcGlasses.class.getResource("view/GlassesViewer.fxml"));
        Parent addGlasses = loader.load();
        Scene scene = new Scene(addGlasses);

        GlassesViewer controller = loader.getController();
        controller.setGlasses(newGlasses);

        resultsBox.getChildren().add(scene.getRoot());
        foundGlasses.add(newGlasses);
    }

    public void printResults(ActionEvent event) {
        var printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(resultsBox.getScene().getWindow())) {

            var printed = false;
            for (Node node : buildPrintText()) {
                if (printerJob.printPage(node)) {
                    printed = true;
                }
            }

            if (printed) {
                printerJob.endJob();
            }
        }
    }

    private List<Node> buildPrintText() {
        var nodes = new ArrayList<Node>();
        var text = new Text();
        var glassesCounter = 0;

        var sb = new StringBuilder();
        text.setFont(PRINTED_FONT);
        var textAdded = false;
        for (Glasses result : foundGlasses) {
            if (glassesCounter++ % GLASSES_PER_PAGE == 0 && glassesCounter > 1) {
                text.setText(sb.toString());
                nodes.add(text);

                sb.setLength(0);
                text = new Text();
                text.setFont(PRINTED_FONT);
                textAdded = true;
            } else {
                textAdded = false;
            }

            sb.append(result.getPrintText());
        }

        if (!textAdded) {
            text.setText(sb.toString());
            nodes.add(text);
        }

        return nodes;
    }

    public void resetParameters(ActionEvent event) {
        // Reset search parameters.
        sphereRange.set(DEFAULT_SPHERE_RANGE);
        cylinderRange.set(DEFAULT_CYLINDER_RANGE);
        axisRange.set(DEFAULT_AXIS_RANGE);
        focalRange.set(DEFAULT_FOCAL_RANGE);
        genderInput.getSelectionModel().select(glasses.getGender());
        ageInput.getSelectionModel().select(glasses.getAge());
        bifocalsInput.getSelectionModel().select(glasses.getBifocals());
    }
}
