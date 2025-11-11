package edu.univalle.cincuentazo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import edu.univalle.cincuentazo.model.GameConfig;

public class StartController {

    @FXML private RadioButton rb1;
    @FXML private RadioButton rb2;
    @FXML private RadioButton rb3;
    @FXML private Button btnStart;

    private ToggleGroup playersGroup;
    private GameConfig config = new GameConfig();
    private int selectedMachines = 0;

    @FXML
    public void initialize() {
        // Crear el grupo
        playersGroup = new ToggleGroup();
        rb1.setToggleGroup(playersGroup);
        rb2.setToggleGroup(playersGroup);
        rb3.setToggleGroup(playersGroup);

        // Asignar eventos
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
    }
}
