package controller;

import view.AlertWindow;

import java.io.IOException;

public class TextNotNormalizedException extends Throwable {

    private String message = "";
    public TextNotNormalizedException() {
        message = "Najskor normalizuj text !";
    }

    public void showAlertWindow() {
        AlertWindow aw = new AlertWindow(message);
        try {
            aw.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getMessage() {
        return message;
    }
}
