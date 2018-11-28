package model.keys;

public class VigenereKey implements Key {

    private String key;

    public VigenereKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
