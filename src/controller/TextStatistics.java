package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TextStatistics {

//    public static Map<String, Double> readNgram(String text, int n, boolean realiveFr) {
//        Map<String, Double> res = new HashMap<>();
//
//        return res;
//    }

    public static Double indexOfCoincidence(Double p[], int n) {
        double res = 0.0;
        for(int i = 0; i < p.length; i++) res += p[i] * (p[i] - 1);

        res /= (n * (n - 1));
        return res;
    }
    public static TreeMap<Character, Double> getSortedCharFrequencies(boolean ascending, String text) {
        Map<Character, Double> charFrequencies = TextStatistics.charFrequencies(text, true);
        ValueComparator vc = new ValueComparator(charFrequencies, ascending);

        TreeMap<Character, Double> sortedCharFreq = new TreeMap<>(vc);
        sortedCharFreq.putAll(charFrequencies);

        return sortedCharFreq;

    }

    public static Map<Character, Double> charFrequencies(String text, boolean relativeFr) {
        Map<Character, Double> frequencies = new HashMap<>();
        for (char ch : text.toCharArray()) {
            if(ch == ' ') continue;
            frequencies.put(ch, (frequencies.getOrDefault(ch, 0.0) + 1.0));
        }
        if (relativeFr) {
            for (Character c : frequencies.keySet())
                frequencies.put(c, frequencies.get(c) / text.length());
        }
        return frequencies;
    }
}
