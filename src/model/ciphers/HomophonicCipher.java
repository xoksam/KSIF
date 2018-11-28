package model.ciphers;

import controller.Text;
import controller.TextNotNormalizedException;
import controller.TextStatistics;
import model.keys.HomophonicKey;
import model.keys.Key;

import java.util.TreeMap;

public class HomophonicCipher implements Cipher {

    private boolean vowels;

    public HomophonicCipher(boolean vowels) {
        this.vowels = vowels;
    }

    @Override
    public String cipher(Key key, String text) throws TextNotNormalizedException {
        if (!(key instanceof HomophonicKey)) throw new IllegalArgumentException("Kluc musi byt typu HomophonicKey");
        String res = "";
        HomophonicKey homoKey = (HomophonicKey) key;
            for (char c : text.toCharArray()) {
                if (c == ' ') {
                    res += ' ';
                    continue;
                }
                if (c - 'a' < 0) throw new TextNotNormalizedException();
                res += getHomoChar(homoKey, c);
            }
        return res;
    }

    @Override
    public String decipher(Key key, String text) {
        String res = "";

        // AH ...
        return res;
    }

    // :D
    private Character getHomoChar(HomophonicKey key, char c) {
        if(vowels) {
            //                                ak je c samohlaska, vrati nahodny znak medzi 0-2, inak 0ty
            return key.getEncryptionKey()[c - 'a'][isVowel(c) ? (int) (Math.random() * (key.getEncryptionKey()[0].length)) : 0];
        }
        else {
            return key.getEncryptionKey()[c - 'a'][isMostFrequent(c) ? (int) (Math.random() * key.getEncryptionKey()[0].length) : 0];
        }
    }

    private boolean isMostFrequent(char c) {
        TreeMap<Character, Double> sortedCharFrequencies = TextStatistics.getSortedCharFrequencies(true, Text.getOpenText());
        Character[] sortedCharFrArr = sortedCharFrequencies.keySet().toArray(new Character[sortedCharFrequencies.size()]);
        String mostFr = "";
        for (int i = 0; i < HomophonicKey.getNumMostFreq(); i++) mostFr += sortedCharFrArr[i].toString();
        return mostFr.contains(Character.toString(c));
    }

    private boolean isVowel(char c) {
        String vowels = "aeiou";
        return vowels.contains(Character.toString(c));
    }
}
