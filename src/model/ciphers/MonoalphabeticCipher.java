package model.ciphers;

import controller.TextNotNormalizedException;
import model.keys.Key;
import model.keys.MonoalphabeticKey;

public class MonoalphabeticCipher implements  Cipher {

    @Override
    public String cipher(Key key, String text) throws TextNotNormalizedException {
        String res = "";

        if(!(key instanceof MonoalphabeticKey)) {
            throw new IllegalArgumentException("Kluc musi byt typu MonoalphabeticKey");
        }
            MonoalphabeticKey monoKey = (MonoalphabeticKey) key;
            for(char c : text.toCharArray()) {
                if(c == ' ') {
                    res += ' ';
                    continue;
                }
                if (c - 'a' < 0) throw new TextNotNormalizedException();
                res += monoKey.getEncryptionKey()[c - 'a'];
            }
        return res;
    }

    @Override
    public String decipher(Key key, String text) {
        String res = "";
        if(!(key instanceof  MonoalphabeticKey)) {
            throw new IllegalArgumentException("Kluc musi byt typu MonoalphabeticKey");
        } else {
            MonoalphabeticKey monoKey = (MonoalphabeticKey) key;
            for(char c : text.toCharArray()) {
                if(c == ' ') {
                    res += ' ';
                    continue;
                }
                if(c >= 'a' && c <= 'z')
                res += monoKey.getDecryptionKey()[c - 'a'];
            }
        }
        return  res;
    }

}
