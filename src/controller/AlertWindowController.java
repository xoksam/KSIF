package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import view.AlertWindow;

import java.net.URL;
import java.util.ResourceBundle;

public class AlertWindowController implements Initializable {

    public Label messageLabel;

    public void close(ActionEvent ae) {
        Button btn = (Button) ae.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageLabel.setText(AlertWindow.getMsg());
    }
}
