package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Pair;
import model.CipherType;
import model.KeyType;
import model.LanguageType;
import model.ciphers.HomophonicCipher;
import model.ciphers.MonoalphabeticCipher;
import model.ciphers.VigenereCipher;
import model.keys.HomophonicKey;
import model.keys.MonoalphabeticKey;
import model.keys.VigenereKey;
import view.AnalyzeWindow;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MainSceneController implements Initializable {

    public ChoiceBox cipherTypeChoiceBox;
    public ChoiceBox cipherChoiceBox;
    public ChoiceBox keyChoiceBox;

    public TextField passTextField;

    public TextArea cipheredTextArea;
    public TextArea decipheredTextArea;

    public Label decipheredTextLenghtLabel;
    public Label cipheredTextLenghtLabel;

    public Button generatePassBtn;

    public CheckBox spacesCipheredChbox;
    public CheckBox spacesDeciphredChbox;
    public CheckBox useKlamaceChBox;
    public CheckBox cipherAgainChBox;

    public RadioButton radioBtn2;
    public RadioButton radioBtn1;
    public RadioButton prob5RadioBtn;
    public RadioButton prob10RadioBtn;

    public Button decipherBtn;
    public Menu textLenMenu;

    public RadioMenuItem slovakRadioMenuItem;
    public RadioMenuItem engRadioMenuItem;
    public RadioMenuItem latinRadioMenuItem;

    private MenuItem[] textLenMenuItems = new MenuItem[5];
    private int[] textLenArr = {100, 250, 500, 750, 1000};

    private ToggleGroup numGroup, probGroup, langGroup;

    private CipherType cType;
    private KeyType kType;
    private LanguageType lType;

    private Character[] password;
    private HomophonicKey homoKey;

    private Pair<CipherType, KeyType> cipherKeyPair;
    private HashMap<Double, Pair<CipherType, KeyType>> pairHashMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cipherTypeChoiceBox.getSelectionModel().selectFirst();
        cipherChoiceBox.setDisable(true);
        keyChoiceBox.setDisable(true);

        cipheredTextArea.textProperty().addListener((observable, oldValue, newValue)
                -> cipheredTextLenghtLabel.setText(Integer.toString(cipheredTextArea.getText().length())));
        decipheredTextArea.textProperty().addListener((observable, oldValue, newValue)
                -> decipheredTextLenghtLabel.setText(Integer.toString(decipheredTextArea.getText().length())));

        numGroup = new ToggleGroup();
        probGroup = new ToggleGroup();
        langGroup = new ToggleGroup();

        radioBtn1.setToggleGroup(numGroup);
        radioBtn2.setToggleGroup(numGroup);

        prob5RadioBtn.setToggleGroup(probGroup);
        prob10RadioBtn.setToggleGroup(probGroup);
        for (int i = 0; i < textLenMenuItems.length; i++)
            textLenMenuItems[i] = new MenuItem(Integer.toString(textLenArr[i]));

        textLenMenu.getItems().addAll(textLenMenuItems);

        slovakRadioMenuItem.setToggleGroup(langGroup);
        engRadioMenuItem.setToggleGroup(langGroup);
        latinRadioMenuItem.setToggleGroup(langGroup);
        for (MenuItem textLenMenuItem : textLenMenuItems) textLenMenuItem.setOnAction(this::handleTextLenSelection);

        pairHashMap = new HashMap<>();

    }

    private void handleTextLenSelection(ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        // default
        File file = new File("languages/def.txt");
        String path = "languages/";
        switch (lType) {
            case SLOVAK:
                path += "SJ/" + (int) (Math.floor(Math.random() * 9)) + "/" + item.getText() + ".TXT";
                break;
            case LATIN:
                path += "LJ/" + (int) (Math.floor(Math.random() * 9)) + "/" + item.getText() + ".TXT";
                break;
            case ENGLISH:
                path += "AJ/" + (int) (Math.floor(Math.random() * 9)) + "/" + item.getText() + ".TXT";
                break;
            case UNKNOWN:
                break;
            default:
                break;
        }
        file = new File(path);
        loadText(file, decipheredTextArea);
    }


    public void handleCipherSelection(ActionEvent actionEvent) {
        ChoiceBox<String> box = (ChoiceBox<String>) actionEvent.getSource();
        int selectedIndex = box.getSelectionModel().getSelectedIndex();
        decipherBtn.setDisable(false);
        if (selectedIndex != 0) {
            cipherChoiceBox.setDisable(false);
            switch (selectedIndex) {
                case 1:     // Monoalfabeticka
                    setChoiceBoxItems(cipherChoiceBox, new String[]{"Monoalfabetická substitúcia"});
                    break;
                case 2:     // Homofonna
                    setChoiceBoxItems(cipherChoiceBox, new String[]{"Homofónna subst. v prípade samohlások",
                            "Homofónna subst. v prípade najfekventov. znakov"});
                    break;
                case 3:     //PolyAlfabeticka
                    setChoiceBoxItems(cipherChoiceBox, new String[]{"Vigenerova šifra"});
                    break;
                default:
                    break;
            }
            cipherChoiceBox.getSelectionModel().selectFirst();
        } else {
            cipherChoiceBox.setItems(FXCollections.observableArrayList("")); // Hmmm ...
            cipherChoiceBox.setDisable(true);
        }
    }

    public void handleKeySelection(ActionEvent actionEvent) {
        ChoiceBox<String> box = (ChoiceBox<String>) actionEvent.getSource();
        int cipherIndex = box.getSelectionModel().getSelectedIndex();
        // Ak je zvoleny monoalfabeticky typ sifry
        keyChoiceBox.setDisable(false);
        if (cipherTypeChoiceBox.getSelectionModel().getSelectedIndex() == 1) {
            switch (cipherIndex) {
                case 0:   // Monoalfabeticka substitucia
                    setChoiceBoxItems(keyChoiceBox, new String[]{"Náhodne heslo", "Náhodné heslo, text rozdelený na 2 časti",
                            "Náhodné heslo, text rozdelený na 3 časti",
                            "Náhodné heslo, text rozdelený na 4 časti"});
                    cType = CipherType.MONOALPHABETIC_SUBSTITUTION;
                    break;
                default:
                    break;
            }
            keyChoiceBox.getSelectionModel().selectFirst();
            // Ak je zvoleny homofonny typ sifry
        } else if (cipherTypeChoiceBox.getSelectionModel().getSelectedIndex() == 2) {
            switch (cipherIndex) {
                case 0:   //Homofony len v pripade samohl.
                    setChoiceBoxItems(keyChoiceBox, new String[]{"Náhodne heslo, homofóny v príp. samohl."});
                    cType = CipherType.HOMOPHONIC_VOVELS;
                    break;
                case 1:
                    setChoiceBoxItems(keyChoiceBox, new String[]{"Náhodne heslo, homofóny v príp. najfrekventov. znakov"});
                    cType = CipherType.HOMOPHONIC_MOST_FREQUENT;
                    break;
                default:
                    break;
            }
            decipherBtn.setDisable(true);
            keyChoiceBox.getSelectionModel().selectFirst();
            // Ak je zvoleny polyalfabeticky typ sifry
        } else if (cipherTypeChoiceBox.getSelectionModel().getSelectedIndex() == 3) {
            switch (cipherIndex) {
                case 0:     // Vigenerova sifra
                    setChoiceBoxItems(keyChoiceBox, new String[]{"Dĺžka hesla 4", "Dĺžka hesla 6", "Dĺžka hesla 8", "Dĺžka hesla 10"});
                    cType = CipherType.VIGENERE;
                    break;
                default:
                    break;
            }
            keyChoiceBox.getSelectionModel().selectFirst();
            // Inak
        } else {
            keyChoiceBox.setDisable(true);
            setChoiceBoxItems(keyChoiceBox, new String[]{});
        }
    }

    public void handleKeys(ActionEvent actionEvent) {
        ChoiceBox<String> box = (ChoiceBox<String>) actionEvent.getSource();
        int selectedIndex = box.getSelectionModel().getSelectedIndex();
        // Ak je zvolena monoalfabeticky typ sifry
        if (cipherTypeChoiceBox.getSelectionModel().getSelectedIndex() == 1) {
            // Monoalfabeticka stubstitucia
            if (cipherChoiceBox.getSelectionModel().getSelectedIndex() == 0) {
//                keyChoiceBox.getSelectionModel().selectFirst();
                generatePassBtn.setDisable(false);
                passTextField.setDisable(false);
                passTextField.setEditable(false);
                passTextField.setOnKeyTyped(event -> {
                    int maxCharacters = 26;
                    if (passTextField.getText().length() >= maxCharacters) event.consume();
                });

                switch (selectedIndex) {
                    case 0:  //Nahodne heslo
                        kType = KeyType.RANDOM_PASS;
                        break;
                    case 1:
                        kType = KeyType.RANDOM_PASS_2;
                        break;
                    case 2:
                        kType = KeyType.RANDOM_PASS_3;
                        break;
                    case 3:
                        kType = KeyType.RANDOM_PASS_4;
                        break;
                    default:
                        break;
                }
            }  // Homofonny typ sifry
        } else if (cipherTypeChoiceBox.getSelectionModel().getSelectedIndex() == 2) {
            generatePassBtn.setDisable(false);
            passTextField.setText("");
            passTextField.setDisable(false);
            passTextField.setEditable(false);
            // Homofonna substitucia, iba samohlasky
            if (cipherChoiceBox.getSelectionModel().getSelectedIndex() == 0) {
                switch (selectedIndex) {
                    case 0:  // iba samohlasky
                        kType = KeyType.HOMOPHONIC_VOVELS;
                        break;
                    default:
                        break;
                }
            } else { // Homofonna sub., najfrekventovanejsie znaky
                switch (selectedIndex) {
                    case 0: //  Najfrekventovanejsie znaky
                        kType = KeyType.HOMOPHONIC_MOST_FREQUENT;
                        break;
                    default:
                        break;
                }
            }
            // Polyalfabeticka substitucia
        } else if (cipherTypeChoiceBox.getSelectionModel().getSelectedIndex() == 3) {
            // VIgenere sifra
            if (cipherChoiceBox.getSelectionModel().getSelectedIndex() == 0) {
                generatePassBtn.setDisable(false);
                passTextField.setText("");
                passTextField.setDisable(false);
                passTextField.setEditable(true);
                passTextField.setOnKeyTyped(event -> {
                    int maxCharacters = 2 * (selectedIndex + 1) + 2;
                    if (passTextField.getText().length() >= maxCharacters) event.consume();
                });
                switch (selectedIndex) {
                    case 0:
                        kType = KeyType.VIGENERE_4;
                        break;
                    case 1:
                        kType = KeyType.VIGENERE_6;
                        break;
                    case 2:
                        kType = KeyType.VIGENERE_8;
                        break;
                    case 3:
                        kType = KeyType.VIGENERE_10;
                        break;
                    default:break;
                }
            }
        } else {
            generatePassBtn.setDisable(true);
            passTextField.setDisable(true);
            passTextField.setEditable(true);
        }
    }

    private void setChoiceBoxItems(ChoiceBox<String> box, String[] items) {
        box.setItems(FXCollections.observableArrayList(items));
    }

    public void quit() {
        System.exit(0);
    }

    public void normalizeCipheredText() {
        normalizeText(cipheredTextArea, spacesCipheredChbox.isSelected());
    }

    public void normalizeDecipheredText() {
        normalizeText(decipheredTextArea, spacesDeciphredChbox.isSelected());
    }

    public void loadCipheredText() {
        File file = Text.openFileChooser();
        loadText(file, cipheredTextArea);
    }

    public void loadDecipheredText() {
        File file = Text.openFileChooser();
        loadText(file, decipheredTextArea);
    }

    private void normalizeText(TextArea textArea, boolean spaces) {
        String norm = Text.convertToTSA(textArea.getText(), spaces);
        textArea.setText(norm);
    }

    private void loadText(File file, TextArea textArea) {
        if (file != null) {
            try {
                String fileContent = new String(Files.readAllBytes(Paths.get(file.toURI())));
                textArea.setText(fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateVigRandPass(KeyType kType) {
        Character[] alphabet = Text.getAlphabet().chars().mapToObj(c -> (char) c).toArray(Character[]::new);
        String pswd = "";
        Character[] passwd;
        switch (kType) {
            case VIGENERE_4:
                passwd = Permutations.rndPerm(alphabet, 4);
                for (Character c : passwd) pswd += c.toString();
                password = passwd;
                break;
            case VIGENERE_6:
                passwd = Permutations.rndPerm(alphabet, 6);
                for (Character c : passwd) pswd += c.toString();
                password = passwd;
                break;
            case VIGENERE_8:
                passwd = Permutations.rndPerm(alphabet, 8);
                for (Character c : passwd) pswd += c.toString();
                password = passwd;
                break;
            case VIGENERE_10:
                passwd = Permutations.rndPerm(alphabet, 10);
                for (Character c : passwd) pswd += c.toString();
                password = passwd;
                break;

                default:break;
        }
    }
    public void generateRandPass() {
        Character[] alphabet = Text.getAlphabet().chars().mapToObj(c -> (char) c).toArray(Character[]::new);
        String pswd = "";
        Text.setOpenText(decipheredTextArea.getText());
        if (cType == CipherType.VIGENERE) {
            Character[] passwd = Permutations.rndPerm(alphabet, 2 * (keyChoiceBox.getSelectionModel().getSelectedIndex() + 1) + 2);
            for (Character c : passwd) pswd += c.toString();
            password = passwd;
        } else {
            Permutations.rndPerm(alphabet);
            for (Character c : alphabet) pswd += c.toString();
            password = alphabet;
        }
        if (cType == CipherType.HOMOPHONIC_VOVELS && kType == KeyType.HOMOPHONIC_VOVELS) {
            homoKey = new HomophonicKey(password, true);
            homoKey.generateEncryptionKey();
        }
        if (cType == CipherType.HOMOPHONIC_MOST_FREQUENT && kType == KeyType.HOMOPHONIC_MOST_FREQUENT) {
            homoKey = new HomophonicKey(password, false);
            homoKey.generateEncryptionKey();
        }
        passTextField.setText(pswd);
    }

    private void addToMap(CipherType cType, KeyType kType, String text) {
        Pair<Double, String> enc = encrypt(cType, kType, text);
        Pair<CipherType, KeyType> pair = new Pair<>(cType, kType);
//        System.out.println(pair);
//        System.out.println(enc);
        pairHashMap.put(enc.getKey(), pair);
    }
    private void cipherWithAllCiphers(String text){
        addToMap(CipherType.MONOALPHABETIC_SUBSTITUTION, KeyType.RANDOM_PASS, text);
        addToMap(CipherType.MONOALPHABETIC_SUBSTITUTION, KeyType.RANDOM_PASS_2, text);
        addToMap(CipherType.MONOALPHABETIC_SUBSTITUTION, KeyType.RANDOM_PASS_3, text);
        addToMap(CipherType.MONOALPHABETIC_SUBSTITUTION, KeyType.RANDOM_PASS_4, text);

        addToMap(CipherType.HOMOPHONIC_VOVELS, KeyType.HOMOPHONIC_VOVELS, text);
        addToMap(CipherType.HOMOPHONIC_MOST_FREQUENT, KeyType.HOMOPHONIC_MOST_FREQUENT, text);

        addToMap(CipherType.VIGENERE, KeyType.VIGENERE_4, text);
        addToMap(CipherType.VIGENERE, KeyType.VIGENERE_6, text);
        addToMap(CipherType.VIGENERE, KeyType.VIGENERE_8, text);
        addToMap(CipherType.VIGENERE, KeyType.VIGENERE_10, text);
    }
    public void encrypt() {
        String openText;
        String encrText = "";
        if (keyChoiceBox.isDisable() || decipheredTextArea.getText() == "" || password == null) return;

        // Zasifruj uz zasifrovany text == selected
        if (cipherAgainChBox.isSelected() && (cipheredTextArea.getText() != "" || cipheredTextArea.getText() != null)) {
            openText = cipheredTextArea.getText();
            generateRandPass();
        } else openText = decipheredTextArea.getText();

        // Pouzi klamace == selected
        if (useKlamaceChBox.isSelected() && !openText.equals("")) {
            TreeMap<Character, Double> leastFreqChars = TextStatistics.getSortedCharFrequencies(false, openText);
            List<Character> leastFrCharsArr = Arrays.asList(leastFreqChars.keySet().toArray(new Character[leastFreqChars.keySet().size()]));

            int numOfKlamace = 1;
            int perc = 5;

            if (numGroup.getSelectedToggle() != null) {
                RadioButton selBtn = (RadioButton) numGroup.getSelectedToggle();
                numOfKlamace = Integer.parseInt(selBtn.getText());
            }
            if (probGroup.getSelectedToggle() != null) {
                RadioButton selBtn = (RadioButton) probGroup.getSelectedToggle();
                perc = Integer.parseInt(selBtn.getText().replaceAll("[%]", ""));
            }
            openText = Text.addKlamace(openText, leastFrCharsArr, numOfKlamace, perc);
        }
        cipherWithAllCiphers(openText);

        Pair<Double, String> pair = encrypt(cType, kType, openText);
        encrText = pair.getValue();
        Pair<CipherType, KeyType> nearest = findNearest(pair.getKey());

        System.out.println(pairHashMap);
        System.out.println(nearest);
        cipheredTextArea.setText(encrText);

    }

    // Vrati IC a k tomu zasifrovany text
    private Pair<Double, String> encrypt(CipherType cType, KeyType kType, String openText) {
        String encrText = "";
        if (cType == CipherType.MONOALPHABETIC_SUBSTITUTION) {
            int textLen = openText.length();
            int numSeg = 1;
            int numOfChars = 0;
            switch (kType) {
                case RANDOM_PASS:
                    numSeg = 1;
                    break;
                case RANDOM_PASS_2:
                    numSeg = 2;
                    break;
                case RANDOM_PASS_3:
                    numSeg = 3;
                    break;
                case RANDOM_PASS_4:
                    numSeg = 4;
                    break;
                default:
                    break;
            }
            try {
                numOfChars = (int) Math.round(textLen / (double) numSeg);
                for (int i = 0; i < numSeg; i++) {
                    generateRandPass();
                    MonoalphabeticCipher monoCipher = new MonoalphabeticCipher();
                    MonoalphabeticKey monoKey = new MonoalphabeticKey(password);
                    int end = numOfChars * (i + 1);
                    if (end > openText.length()) end = openText.length();
                    String segment = openText.substring(numOfChars * i, end);
                    encrText += monoCipher.cipher(monoKey, segment) + " " + Arrays.toString(monoKey.getEncryptionKey()) + "\n";
                }
            } catch (TextNotNormalizedException e) {
                e.showAlertWindow();
            }
        }
        if (cType == CipherType.HOMOPHONIC_VOVELS && kType == KeyType.HOMOPHONIC_VOVELS) {
            try {
                if (homoKey == null) {
                    homoKey = new HomophonicKey(password, true);
                    homoKey.generateEncryptionKey();
                }
                HomophonicCipher homoCipher = new HomophonicCipher(true);
                encrText = homoCipher.cipher(homoKey, openText);
            } catch (TextNotNormalizedException e) {
                e.showAlertWindow();
            }
        }
        if (cType == CipherType.HOMOPHONIC_MOST_FREQUENT && kType == KeyType.HOMOPHONIC_MOST_FREQUENT) {
            if (homoKey == null) {
                homoKey = new HomophonicKey(password, false);
                homoKey.generateEncryptionKey();
            }
            try {
                HomophonicCipher homoCipher = new HomophonicCipher(false);
                encrText = homoCipher.cipher(homoKey, openText);
            } catch (TextNotNormalizedException e) {
                e.showAlertWindow();
            }
        }
        if (cType == CipherType.VIGENERE) {
            String pass = passTextField.getText();
            generateVigRandPass(kType);

            if (pass.equals("")) return new Pair<>(0.0, "");
            try {
                generateRandPass();
                VigenereCipher vigCiph = new VigenereCipher();
                VigenereKey vigKey = new VigenereKey(pass);
                encrText = vigCiph.cipher(vigKey, openText);
            } catch (TextNotNormalizedException e) {
                e.showAlertWindow();
            }
        }
        Map<Character, Double> absoluteEncrTextFr = TextStatistics.charFrequencies(encrText, false);
        Double ic = TextStatistics.indexOfCoincidence(absoluteEncrTextFr.values().toArray(new Double[absoluteEncrTextFr.size()]),
                encrText.length());

        Pair<Double, String> res = new Pair<>(ic, encrText);
        return res;
    }
    private Pair<CipherType, KeyType> findNearest(double ic) {
        double minDistance = 1;
        Pair<CipherType, KeyType> best = null;
        for (Map.Entry<Double, Pair<CipherType, KeyType>> entrySet : pairHashMap.entrySet()) {
            Double key = entrySet.getKey();
            Pair<CipherType, KeyType> value = entrySet.getValue();
            double dist = Math.abs(ic - key);
            if (dist < minDistance) {
                minDistance = dist;
                best = value;
            }
        }
        return best;
    }

    public void decrypt() {
        if (keyChoiceBox.isDisable() || cipheredTextArea.getText() == "") return;
        String encrText = cipheredTextArea.getText();
        String openText = "";

        if (cType == CipherType.MONOALPHABETIC_SUBSTITUTION && kType == KeyType.RANDOM_PASS) {
            if (password == null) return;
            MonoalphabeticCipher monoCipher = new MonoalphabeticCipher();
            MonoalphabeticKey monoKey = new MonoalphabeticKey(password);
            openText = monoCipher.decipher(monoKey, encrText);
        }
        if (cType == CipherType.VIGENERE) {
            String pass = passTextField.getText();
            if (pass == "" || pass == null) return;

            VigenereCipher vigCiph = new VigenereCipher();
            VigenereKey vigKey = new VigenereKey(pass);
            openText = vigCiph.decipher(vigKey, encrText);
        }
        decipheredTextArea.setText(openText);
    }

    public void analyze() throws IOException {
        Text.setOpenTextLen(Integer.parseInt(decipheredTextLenghtLabel.getText()));
        Text.setEncrTextLen(Integer.parseInt(cipheredTextLenghtLabel.getText()));
        Text.setOpenText(decipheredTextArea.getText());
        Text.setEncText(cipheredTextArea.getText());
        Map<Character, Double> relativeOpenTextFr = TextStatistics.charFrequencies(decipheredTextArea.getText(), true);
        Map<Character, Double> relativeEncrTextFr = TextStatistics.charFrequencies(cipheredTextArea.getText(), true);
        Map<Character, Double> absoluteEncrTextFr = TextStatistics.charFrequencies(cipheredTextArea.getText(), false);
        Map<Character, Double> absoluteOpenTextFr = TextStatistics.charFrequencies(decipheredTextArea.getText(), false);
        AnalyzeWindow analyzeWindow = new AnalyzeWindow(relativeOpenTextFr, relativeEncrTextFr, absoluteOpenTextFr, absoluteEncrTextFr);
        analyzeWindow.start();

    }

    public void setupRadioBtns(ActionEvent actionEvent) {
        CheckBox box = (CheckBox) actionEvent.getSource();
        radioBtn2.setDisable(!box.isSelected());
        radioBtn1.setDisable(!box.isSelected());
        prob10RadioBtn.setDisable(!box.isSelected());
        prob5RadioBtn.setDisable(!box.isSelected());
        radioBtn1.setSelected(box.isSelected());
        prob5RadioBtn.setSelected(box.isSelected());
    }

    public void selectLang(ActionEvent ae) {
        RadioMenuItem selItem = (RadioMenuItem) ae.getSource();
        if (selItem.equals(slovakRadioMenuItem)) lType = LanguageType.SLOVAK;
        else if (selItem.equals(engRadioMenuItem)) lType = LanguageType.ENGLISH;
        else if (selItem.equals(latinRadioMenuItem)) lType = LanguageType.LATIN;
        else lType = LanguageType.UNKNOWN;
    }
}
