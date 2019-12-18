package com.kropiejohn.ftc.glasses;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main javafx application for FTC eyeglasses clinics.
 */
public class FtcGlasses extends Application {
    public static final String TITLE = "FTC Glasses";
    private static Stage stage;
    private static Scene homeScene;


    /**
     * Main method.
     *
     * @param args program arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Used to start the ftc glasses application.
     *
     * @param primaryStage the primary stage.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        goHome();
        stage.show();
    }

    /**
     * Will navigate back to the home screen.
     *
     * @throws Exception unexpected.
     */
    public static void goHome() throws Exception {
        if (homeScene == null) {
            Parent home = FXMLLoader.load(FtcGlasses.class.getResource("view/Home.fxml"));
            homeScene = new Scene(home);
        }

        stage.setScene(homeScene);
        stage.setTitle("FTC Glasses");
    }

    /**
     * Will return the primary stage.
     *
     * @return the primary stage for the application.
     */
    public static Stage getStage() {
        return stage;
    }
}
