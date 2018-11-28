package model.ciphers;

import controller.TextNotNormalizedException;
import model.keys.Key;
import model.keys.VigenereKey;

public class VigenereCipher implements Cipher {

    @Override
    public String cipher(Key key, String text) throws TextNotNormalizedException {
        if(!(key instanceof VigenereKey)) throw new IllegalArgumentException("Kluc musi byt typu VigenereKey!");

        VigenereKey vigKey = (VigenereKey) key;
        String res = "";

        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if(text.charAt(i) == ' ') {
                res += ' ';
                continue;
            }
//            if(c >= '0' && c <= '9') res += c;
            if(c - 'a' < 0) throw new TextNotNormalizedException();
//            if (c < 'a' || c > 'z') continue;
            if(c >= 'a' && c <= 'z') {
                res += (char)((c + vigKey.getKey().charAt(j) - 2 * 'a') % 26 + 'a');
                j = ++j % vigKey.getKey().length();
            }
        }
        return res;
    }

    @Override
    public String decipher(Key key, String text) {
        if(!(key instanceof VigenereKey)) throw new IllegalArgumentException("Kluc musi byt typu VigenereKey!");

        String res = "";
        VigenereKey vigKey = (VigenereKey) key;

        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
//            if (c < 'a' || c > 'z') continue;
//            if(c >= '0' && c <= '9') {
//                res += c;
//                continue;
//            }
            if(text.charAt(i) == ' ') {
                res += ' ';
                continue;
            }
            if(c >= 'a' && c <= 'z'){
                res += (char)((c - vigKey.getKey().charAt(j) + 26) % 26 + 'a');
                j = ++j % vigKey.getKey().length();
            }
        }
        return res;
    }

}


