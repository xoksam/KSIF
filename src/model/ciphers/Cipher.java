package model.ciphers;

import controller.TextNotNormalizedException;
import model.keys.Key;

public interface Cipher {
    String cipher(Key key, String text) throws TextNotNormalizedException;
    String decipher(Key key, String text);
}
