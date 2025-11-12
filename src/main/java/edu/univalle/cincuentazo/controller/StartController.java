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

public class StartController {

    @FXML private RadioButton rb1;
    @FXML private RadioButton rb2;
    @FXML private RadioButton rb3;
    @FXML private Button btnStart;
    @FXML private ToggleGroup playersGroup;

    private GameConfig config = GameConfig.getInstance();

    private int selectedMachines = 0;

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

    private void handleSelection(int machines) {
        selectedMachines = machines;
        config.setMachinePlayers(machines);
        btnStart.setDisable(false);
    }

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
