package edu.univalle.cincuentazo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import edu.univalle.cincuentazo.view.StartStage;

/**
 * Main class of the Cincuentazo application.
 * <p>
 * This class extends {@link javafx.application.Application} and is responsible
 * for launching the JavaFX application. When executed, it loads the initial
 * window through the {@link edu.univalle.cincuentazo.view.StartStage} class.
 * </p>
 *
 * <p>
 * It contains the {@link #main(String[])} method which launches the JavaFX application.
 * </p>
 *
 * @version 1.0
 */
public class Main extends Application {

    /**
     * Method called when the JavaFX application starts.
     * <p>
     * Creates an instance of {@link edu.univalle.cincuentazo.view.StartStage}
     * to display the initial window of the application.
     * </p>
     *
     * @param primaryStage The primary stage provided by JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        new StartStage();
    }

    /**
     * Entry point of the application.
     * <p>
     * This method launches the JavaFX application and internally calls
     * {@link #start(Stage)}.
     * </p>
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}
