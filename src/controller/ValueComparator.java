package controller;

import java.util.Comparator;
import java.util.Map;

public class ValueComparator implements Comparator<Character> {
    private Map<Character, Double> base;
    private boolean ascending;

    public ValueComparator(Map<Character, Double> base, boolean ascending) {
        this.base = base;
        this.ascending = ascending;
    }

    @Override
    public int compare(Character a, Character b) {
        if(ascending){
            if (base.get(a) >= base.get(b)) return -1;
            else return 1;
        } else {
            if (base.get(a) <= base.get(b)) return -1;
            else return 1;
        }
    }
}
