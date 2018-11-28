package controller;

import javafx.stage.FileChooser;

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Text {

    private static int openTextLen;
    private static int encrTextLen;

    private static String openText, encText;

    public static File openFileChooser() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Nacitaj zasifrovany text");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Textove Subory", "*.txt"),
                new FileChooser.ExtensionFilter("Vsetky subory", "*.*")
        );
        File file = fc.showOpenDialog(null);
        return file;
    }

    public static String convertToTSA(String text, boolean keepSpace) {
        text = text.toLowerCase();
        text = Normalizer.normalize(text, Normalizer.Form.NFD);

        text = text.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        text = text.replaceAll("\\[(.*?)\\]", "");
        text = text.replaceAll("[^a-z ]","");
        if(!keepSpace)
            text = text.replaceAll("\\s+", "");

        return text;
    }

    private static String addCharToString(String str, int index, char c) {
        str = str.substring(0, index) + Character.toString(c) + str.substring(index, str.length());
        return str;
    }

    public static String addKlamace(String text, List<Character> leastFrCharsArr, int n, int percentage) {
        int num = (int) ((text.length() / 100.0) * percentage * n);

        List<Character> alphabet = Arrays.asList(Text.getAlphabet().chars().mapToObj(c -> (char) c).toArray(Character[]::new));
        ArrayList<Integer> randIndices = randNums(num, text.length());
        ArrayList<Character> characters = new ArrayList<>();

        characters.addAll(alphabet);
          // ak ma abeceda pouzita v texte menej znakov ako TSA
        if (leastFrCharsArr.size() < (alphabet.size() - n)) {
//            characters.addAll(alphabet);
            characters.removeAll(leastFrCharsArr);
            // zostanu mi tu len znaky ktore sa v texte nepouzivaju
        }

        for (int i = 0; i < randIndices.size(); i++)
            text = addCharToString(text, randIndices.get(i), characters.get(i % n));

        return text;
    }

    private static ArrayList<Integer> randNums(int n, int range) {
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            res.add((int) (Math.random() * range));
        }
        Collections.shuffle(res);

        return res;
    }
    public static String getAlphabet() {
        String res = "";
        for(int i = 'a'; i <= 'z'; i++) {
            res += (char) i;
        }
        return res;
    }
    public static Character[] getCharacterAlphabet(){
        return Text.getAlphabet().chars().mapToObj(c -> (char)c).toArray(Character[]::new);
    }

    static void setEncrTextLen(int encrTextLen) {
        Text.encrTextLen = encrTextLen;
    }
    static void setOpenTextLen(int openTextLen) {
        Text.openTextLen = openTextLen;
    }
    static int getOpenTextLen() {
        return openTextLen;
    }
    static int getEncrTextLen() {
        return encrTextLen;
    }
    static String getEncText() {
        return encText;
    }
    static void setEncText(String encText) {
        Text.encText = encText;
    }
    public static String getOpenText() { return openText; }
    static void setOpenText(String openText) {Text.openText = openText; }

}
