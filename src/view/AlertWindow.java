package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AlertWindow {

    private static String msg;
    private static final int WIDTH = 520;
    private static final int HEIGHT = 140;

    public AlertWindow(String msg) {
        AlertWindow.msg = msg;
    }

    public static String getMsg() {
        return msg;
    }

    public void start() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../resources/alert_window.fxml"));
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
