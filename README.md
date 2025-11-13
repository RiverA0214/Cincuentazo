# Cincuentazo

Cincuentazo es un juego de cartas digital basado en reglas similares al popular juego de "50", desarrollado en **Java** con **JavaFX** para la interfaz gráfica. Este proyecto implementa tanto jugadores humanos como jugadores controlados por máquina (IA simple).

---

## 🎮 Características

- Juego de cartas con reglas para sumar hasta 50.
- Jugadores humanos y máquinas (1 a 3 jugadores máquina).
- Turnos automáticos para jugadores máquina.
- Control de eliminaciones de jugadores cuando no pueden jugar.
- Elección de valor de As (1 o 10) para los jugadores humanos.
- Interfaz gráfica interactiva con visualización de cartas.
- Mazo de cartas con barajado y gestión de robo automático.
- Mensajes temporales y alertas de juego.

---

## 📁 Estructura del proyecto

edu.univalle.cincuentazo

-model # Clases del modelo de juego (Game, Player, Card, etc.)
-controller # Controladores para la interacción GUI
-view # Vistas FXML y componentes visuales
-Main.java # Clase principal para iniciar la aplicación

---

## Uso

-Selecciona el número de jugadores máquina en la pantalla de inicio (1 a 3).

-Pulsa Iniciar para comenzar el juego.

-Juega tu turno seleccionando una carta de tu mano.

-Si juegas un As, elige su valor (1 o 10).

-Roba una carta del mazo después de jugar.

-El juego termina cuando solo queda un jugador activo. Se mostrará el ganador.
