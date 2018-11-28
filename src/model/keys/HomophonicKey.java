package model.keys;

import controller.Text;
import controller.TextStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeMap;

public class HomophonicKey implements Key {

    private Character[][] encryptionKey;
    private Character[] alphabetPerm;
    private boolean mode;
    private static int maxCharOpt = 3;
    private static int numMostFreq = 5;

        // mode == 1 -> Iba samohlasky, mode == 0 -> Iba najfkventovanejsie znaky
    public HomophonicKey(Character[] alphabetPerm, boolean mode) {
        this.mode = mode;
        this.alphabetPerm = alphabetPerm;
        encryptionKey = new Character[alphabetPerm.length][maxCharOpt];
    }

    public void generateEncryptionKey() {
        ArrayList<Integer> randNums = randNums(10);
        ArrayList<Integer> toRemove = new ArrayList<>();
        if(mode){
            // a, e, i, o, u
            int[] vowelIndices = {0, 4, 8, 14, 20};
            for (int i = 0; i < encryptionKey.length; i++) {
                encryptionKey[i][0] = alphabetPerm[i];
                encryptionKey[vowelIndices[i % vowelIndices.length]][1] = Character.forDigit(randNums.get(i % (randNums.size())), 10);
            }
            for (int i = 0; i < vowelIndices.length; i++) toRemove.add((int) encryptionKey[vowelIndices[i]][1] - 48);
            randNums.removeAll(toRemove);

            for (int i = 0; i < encryptionKey.length; i++) {
                encryptionKey[vowelIndices[i % vowelIndices.length]][2] = Character.forDigit(randNums.get(i % (randNums.size())), 10);
                System.out.println(Arrays.toString(encryptionKey[i]));
            }
        }
        ///////////////////////////////////////////////////////
        else {
            int j = 0;
            TreeMap<Character, Double> sortedCharFrequencies = TextStatistics.getSortedCharFrequencies(true, Text.getOpenText());
            Character[] sortedCharFrArr = sortedCharFrequencies.keySet().toArray(new Character[sortedCharFrequencies.size()]);
            for (int i = 0; i < encryptionKey.length; i++) {
                encryptionKey[i][0] = alphabetPerm[i];
                encryptionKey[indexOf(Text.getCharacterAlphabet(),sortedCharFrArr[j++ % numMostFreq])][1]
                        = Character.forDigit(randNums.get(i % randNums.size()), 10);
            }
            for (int i = 0; i < encryptionKey.length; i++)
                if (encryptionKey[i][1] != null) toRemove.add(Integer.parseInt(encryptionKey[i][1].toString()));

            randNums.removeAll(toRemove);
            j = 0;
            for (int i = 0; i < encryptionKey.length; i++) {
                encryptionKey[indexOf(Text.getCharacterAlphabet(), sortedCharFrArr[j++ % numMostFreq])][2]
                        = Character.forDigit(randNums.get(i % randNums.size()), 10);
                System.out.println(Arrays.toString(encryptionKey[i]));
            }
        }
    }

    public Character[][] getEncryptionKey() {
        return encryptionKey;
    }

    private <T> int indexOf(T[] arr, T val) {
        return Arrays.asList(arr).indexOf(val);
    }

//    private int indexOf(Character[] arr, Character val) {
//        return IntStream.range(0, arr.length).filter(i -> arr[i] == val).findFirst().orElse(-1);
//    }

    // Vrati arraylist nahodnych cisel <0,n)
    private ArrayList<Integer> randNums(int n) {
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = 0; i < n; i++) res.add(i);
        Collections.shuffle(res);

        return res;
    }

    public static int getNumMostFreq() {
        return numMostFreq;
    }
}
