package edu.univalle.cincuentazo.view;

import edu.univalle.cincuentazo.controller.StartController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Represents the start stage of the Cincuentazo game.
 * <p>
 * This class extends {@link Stage} and is responsible for launching the
 * start menu of the game. It loads the FXML view
 * {@code cincuentazo-start-view.fxml} and automatically connects it
 * to the {@link StartController} defined in the FXML.
 * </p>
 *
 * <p>
 * The window is titled "Cincuentazo - Player Selection" and is not resizable.
 * </p>
 *
 * @see StartController
 * @since 1.0
 */
public class StartStage extends Stage {

    /**
     * Constructs a new StartStage and displays the start menu.
     * <p>
     * Loads the FXML layout, sets the scene, window title, and shows the stage.
     * Any exceptions during loading are caught and printed to the error stream.
     * </p>
     */
    public StartStage() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/edu/univalle/cincuentazo/view/cincuentazo-start-view.fxml")
            );

            Scene scene = new Scene(loader.load());

            setTitle("Cincuentazo - Selección de jugadores");
            setResizable(false);
            setScene(scene);
            show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista de inicio (StartStage).");
        }
    }
}
