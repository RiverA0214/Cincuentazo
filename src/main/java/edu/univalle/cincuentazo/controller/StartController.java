package edu.univalle.cincuentazo.controller;

import edu.univalle.cincuentazo.model.GameConfig;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller class for the start menu of the Cincuentazo game.
 * <p>
 * Manages the selection of the number of machine players (1 to 3) and
 * handles starting the main game window. Updates the {@link GameConfig}
 * singleton with the selected machine player count.
 * </p>
 *
 * <p>Responsibilities include:</p>
 * <ul>
 *     <li>Initializing radio buttons for machine player selection</li>
 *     <li>Enabling/disabling the start button based on selection</li>
 *     <li>Launching the game scene and passing the selected configuration to {@link GameController}</li>
 * </ul>
 *
 * @see GameConfig
 * @see GameController
 * @since 1.0
 */
public class StartController {

    @FXML private RadioButton rb1;
    @FXML private RadioButton rb2;
    @FXML private RadioButton rb3;
    @FXML private Button btnStart;
    @FXML private ToggleGroup playersGroup;

    /** Singleton game configuration instance. */
    private GameConfig config = GameConfig.getInstance();

    /** Currently selected number of machine players. */
    private int selectedMachines = 0;

    /**
     * Initializes the controller.
     * <p>
     * Sets up the radio buttons in a toggle group, assigns event handlers
     * for selection, and configures the start button.
     * </p>
     */
    @FXML
    public void initialize() {
        // Crear grupo de botones
        playersGroup = new ToggleGroup();
        rb1.setToggleGroup(playersGroup);
        rb2.setToggleGroup(playersGroup);
        rb3.setToggleGroup(playersGroup);

        // Eventos de selección
        rb1.setOnAction(e -> handleSelection(1));
        rb2.setOnAction(e -> handleSelection(2));
        rb3.setOnAction(e -> handleSelection(3));

        btnStart.setDisable(true);
        btnStart.setOnAction(e -> startGame());
    }

    /**
     * Handles selection of the number of machine players.
     *
     * @param machines the number of machine players selected
     */
    private void handleSelection(int machines) {
        selectedMachines = machines;
        config.setMachinePlayers(machines);
        btnStart.setDisable(false);
    }

    /**
     * Starts the main game window with the selected number of machine players.
     * <p>
     * Loads the game FXML, obtains the {@link GameController} instance,
     * and passes the number of machines. Then it switches the current stage
     * to display the game scene.
     * </p>
     */
    private void startGame() {
        System.out.println("Iniciando juego con " + selectedMachines + " jugadores máquina.");

        try {
            // Cargar la vista del juego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/univalle/cincuentazo/view/cincuentazo-game-view.fxml"));
            Scene scene = new Scene(loader.load());

            // Obtener el controlador del juego y pasarle el número de máquinas
            GameController controller = loader.getController();
            controller.startGame(selectedMachines);

            // Cambiar la ventana actual
            Stage stage = (Stage) btnStart.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Cincuentazo");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista del juego.");
        }
    }
}
