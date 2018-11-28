package controller;

import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import view.AnalyzeWindow;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class AnalyzeWindowController implements Initializable {


    public BarChart<String, Double> encrTextBarChart;
    public BarChart<String, Double> openTextBarChart;

    public Label openLanguageLabel;
    public Label openIcLabel;

    public Label encLanguageLabel;
    public Label encIcLabel;

    private Map<Character, Double> relEncrTextFreq = AnalyzeWindow.getRelativeEncrTextFreq();
    private Map<Character, Double> relOpenTextFreq = AnalyzeWindow.getRelativeOpenTextFreq();

    private Map<Character, Double> absEncrTextFreq = AnalyzeWindow.getAbsoluteEncrTextFreq();
    private Map<Character, Double> absOpenTextFreq = AnalyzeWindow.getAbsoluteOpenTextFreq();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initBarChart(encrTextBarChart, relEncrTextFreq);
        initBarChart(openTextBarChart, relOpenTextFreq);
        // :D
        Double openIC = TextStatistics.indexOfCoincidence(
                absOpenTextFreq.values().toArray(new Double[absOpenTextFreq.values().size()]), Text.getOpenTextLen());

        openIcLabel.setText(Double.toString(openIC));

        Double encIC = TextStatistics.indexOfCoincidence(
                absEncrTextFreq.values().toArray(new Double[absEncrTextFreq.values().size()]), Text.getEncrTextLen());

        encIcLabel.setText(Double.toString(encIC));

        encLanguageLabel.setText(Language.guessLanguage(Text.getEncText()).toString());
        openLanguageLabel.setText(Language.guessLanguage(Text.getOpenText()).toString());

    }

    private void initBarChart(BarChart<String, Double> chart, Map<Character, Double> map) {
        XYChart.Series<String, Double> series1 = new XYChart.Series<>();
        for (Character c : map.keySet()) {
            series1.getData().add(new XYChart.Data<>(c.toString(), map.get(c)));
        }

        chart.getData().addAll(series1);
    }

    }
