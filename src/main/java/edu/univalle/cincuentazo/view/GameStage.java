package edu.univalle.cincuentazo.view;

import edu.univalle.cincuentazo.controller.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Represents the main game window of the Cincuentazo application.
 * <p>
 * This class extends {@link Stage} and is responsible for launching the
 * main game interface. It loads the FXML layout
 * {@code cincuentazo-game-view.fxml} and connects it with
 * {@link GameController}.
 * </p>
 *
 * <p>
 * The window is titled "Cincuentazo - In Game" and is not resizable.
 * The number of machine players is passed to the controller upon creation.
 * </p>
 *
 * @see GameController
 * @since 1.0
 */
public class GameStage extends Stage {

    /**
     * Constructs a new GameStage and starts the game.
     * <p>
     * Loads the game FXML, sets the scene, window title, and shows the stage.
     * It also passes the number of machine players to the controller.
     * Any exceptions during loading are caught and printed to the error stream.
     * </p>
     *
     * @param machineCount the number of machine players in the game
     */
    public GameStage(int machineCount) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/edu/univalle/cincuentazo/view/cincuentazo-game-view.fxml")
            );
            Scene scene = new Scene(loader.load());


            GameController controller = loader.getController();
            controller.startGame(machineCount);

            setTitle("Cincuentazo - En juego");
            setResizable(false);
            setScene(scene);
            show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista del juego (GameStage).");
        }
    }
}

