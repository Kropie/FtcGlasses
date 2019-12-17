package com.kropiejohn.ftc.glasses.controller;

import com.kropiejohn.ftc.glasses.FtcGlasses;
import com.kropiejohn.ftc.glasses.control.IntegerTextField;
import com.kropiejohn.ftc.glasses.model.Glasses;
import com.kropiejohn.ftc.glasses.model.GlassesDatabase;
import com.kropiejohn.ftc.glasses.util.EnhancedNumberStringConverter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

/**
 * Home controller.
 */
public class Home {
    @FXML
    public IntegerTextField editIndexInput;

    @FXML
    public Button editButton;

    private final IntegerProperty editGlassesProperty = new SimpleIntegerProperty(1);

    @FXML
    public IntegerTextField removeIndexInput;

    @FXML
    public Button removeButton;

    private final IntegerProperty removeGlassesProperty = new SimpleIntegerProperty(1);

    private final ObservableMap<Integer, Glasses> db = GlassesDatabase.INSTANCE.get();

    private Scene findGlassesScene;

    private GlassesExcelParser glassesExcelParser;

    @FXML
    public void initialize() {
        editIndexInput.textProperty().bindBidirectional(editGlassesProperty, new EnhancedNumberStringConverter());
        removeIndexInput.textProperty().bindBidirectional(removeGlassesProperty, new EnhancedNumberStringConverter());
        glassesExcelParser = new GlassesExcelParser();
    }

    @FXML
    protected void addGlasses(final ActionEvent event) throws Exception {
        addUpdateGlasses(UpdateType.CREATE, GlassesDatabase.INSTANCE.getNewIndex());
    }

    @FXML
    public void editGlasses(ActionEvent event) throws Exception {
        if (!db.containsKey(editGlassesProperty.getValue())) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            if (db.isEmpty()) {
                alert.setContentText("No glasses currently in inventory.");
            } else {
                alert.setContentText("No glasses are available at inventory number of " + editGlassesProperty.get()
                        + System.lineSeparator() + "Valid range is from 1 - " + (GlassesDatabase.INSTANCE.getNewIndex() - 1));
            }

            alert.showAndWait();
        } else if (db.get(editGlassesProperty.getValue()).isRemoved()) {
            // The glasses have been removed. Give the user the chance to restore the glasses.
            var alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("The glasses at inventory number " + editGlassesProperty.get() + " have been removed."
                    + System.lineSeparator() + "Would you like to restore them?");

            var doRestore = alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();

            if (doRestore) {
                db.get(editGlassesProperty.get()).setRemoved(false);
                // Now that the glasses have been restored try again.
                editGlasses(event);
            }
        } else {
            addUpdateGlasses(UpdateType.UPDATE, editGlassesProperty.get());
        }
    }

    private void addUpdateGlasses(UpdateType updateType, int glassesNumber) throws Exception {
        var loader = new FXMLLoader(FtcGlasses.class.getResource("view/AddUpdateGlasses.fxml"));
        Parent addGlasses = loader.load();
        Scene scene = new Scene(addGlasses);

        AddUpdateGlasses controller = loader.getController();
        controller.setUpdateType(updateType, glassesNumber);

        FtcGlasses.getStage().setScene(scene);
        FtcGlasses.getStage().setTitle(FtcGlasses.TITLE + " - " + updateType.getName() + " Glasses");
    }

    public void findGlasses(ActionEvent event) throws Exception {
        if (findGlassesScene == null) {
            var loader = new FXMLLoader(FtcGlasses.class.getResource("view/FindGlasses.fxml"));
            Parent addGlasses = loader.load();
            findGlassesScene = new Scene(addGlasses);
        }

        FtcGlasses.getStage().setScene(findGlassesScene);
        FtcGlasses.getStage().setTitle(FtcGlasses.TITLE + " - Find Glasses");
    }

    public void removeGlasses(ActionEvent event) throws Exception {
        if (!db.containsKey(removeGlassesProperty.getValue())) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            if (db.isEmpty()) {
                alert.setContentText("No glasses currently in inventory.");
            } else {
                alert.setContentText("No glasses are available at inventory number of " + removeGlassesProperty.get()
                        + System.lineSeparator() + "Valid range is from 1 - " + (GlassesDatabase.INSTANCE.getNewIndex() - 1));
            }

            alert.showAndWait();
        } else {
            var alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Inventory Number - " + removeGlassesProperty.get());
            alert.setContentText("Are you sure want to remove the glasses?");
            var doRemoval = alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
            if (doRemoval) {
                db.get(removeGlassesProperty.get()).setRemoved(true);
            }
        }
    }

    public void importGlasses(ActionEvent event) throws IOException, InvalidFormatException {
        glassesExcelParser.importDatabase(FtcGlasses.getStage());
    }
}
