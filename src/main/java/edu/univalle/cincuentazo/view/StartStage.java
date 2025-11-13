package edu.univalle.cincuentazo.view;

import edu.univalle.cincuentazo.controller.StartController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Launches the start menu of the game Cincuentazo.
 * Loads the FXML view and connects it to StartController.
 */
public class StartStage extends Stage {

    public StartStage() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/edu/univalle/cincuentazo/view/cincuentazo-start-view.fxml")
            );

            // El controlador ya se declara en el FXML, así que no lo seteamos aquí
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
