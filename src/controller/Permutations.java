package controller;

import java.util.Arrays;
import java.util.Collections;

public class Permutations {


    public static void rndPerm(Object in[]) {
        Collections.shuffle(Arrays.asList(in));
    }
    public static Character[] rndPerm(Character in[], int n) {
        Character[] res = new Character[n];
        for (int i = 0; i < n; i++) {
            int index = (int) Math.floor(Math.random() * in.length);
            res[i] = in[index];
        }
        return res;
    }

    public static Character[] inverse(Character[] perm) {
        Character[] inv = new Character[perm.length];
        for (int i = 0; i < perm.length; i++) {
            inv[perm[i] - 'a'] = (char) (i + 'a');
        }
        return inv;
    }
}
