package model.keys;

import controller.Permutations;

public class MonoalphabeticKey implements Key {

    private Character[] encryptionKey;
    private Character[] decryptionKey;
    public  MonoalphabeticKey(Character[] encryptionKey){
        this.encryptionKey = encryptionKey;
        decryptionKey = Permutations.inverse(encryptionKey);
    }
    public Character[] getEncryptionKey() {
        return encryptionKey;
    }

    public Character[] getDecryptionKey() {
        return decryptionKey;
    }
}
