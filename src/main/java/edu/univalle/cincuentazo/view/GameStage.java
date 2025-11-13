package edu.univalle.cincuentazo.view;

import edu.univalle.cincuentazo.controller.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Represents the main game window of Cincuentazo.
 * Loads the game FXML and links it with GameController.
 */
public class GameStage extends Stage {

    public GameStage(int machineCount) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/edu/univalle/cincuentazo/view/cincuentazo-game-view.fxml")
            );
            Scene scene = new Scene(loader.load());

            // Pasar la cantidad de jugadores máquina al controlador
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

