package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class AnalyzeWindow {
    private static Map<Character, Double> relativeOpenTextFreq;
    private static Map<Character, Double> relativeEncrTextFreq;

    private static Map<Character, Double> absoluteEncrTextFreq;
    private static Map<Character, Double> absoluteOpenTextFreq;

    private static final double WIDTH = 1000;
    private static final double HEIGHT = 700;


    public AnalyzeWindow(Map<Character, Double> relativeOpenTextFr, Map<Character, Double> relativeEncrTextFr,
                         Map<Character, Double> absoluteOpenTextFreq, Map<Character, Double> absoluteEncrTextFreq) {
        AnalyzeWindow.relativeOpenTextFreq = relativeOpenTextFr;
        AnalyzeWindow.relativeEncrTextFreq = relativeEncrTextFr;

        AnalyzeWindow.absoluteOpenTextFreq = absoluteOpenTextFreq;
        AnalyzeWindow.absoluteEncrTextFreq = absoluteEncrTextFreq;
    }

    public void start() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../resources/analyzeScene.fxml"));
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public static Map<Character, Double> getAbsoluteEncrTextFreq() {
        return absoluteEncrTextFreq;
    }

    public static Map<Character, Double> getAbsoluteOpenTextFreq() {
        return absoluteOpenTextFreq;
    }

    public static Map<Character, Double> getRelativeEncrTextFreq() {
        return relativeEncrTextFreq;
    }

    public static Map<Character, Double> getRelativeOpenTextFreq() {
        return relativeOpenTextFreq;
    }
}
