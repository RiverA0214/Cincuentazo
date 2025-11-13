package edu.univalle.cincuentazo.exceptions;

public class DeckEmptyException extends RuntimeException {
    public DeckEmptyException(String message) {
        super(message);
    }
}
