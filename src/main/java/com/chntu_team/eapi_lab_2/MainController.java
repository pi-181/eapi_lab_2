package com.chntu_team.eapi_lab_2;

import com.chntu_team.eapi_lab_2.util.FxUtil;
import com.chntu_team.eapi_lab_2.util.NumberUtil;
import com.chntu_team.eapi_lab_2.util.StringUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;

public class MainController {
    private final AffineCaesarCipher cipher = new AffineCaesarCipher();

    @FXML
    private TextField keyTInput;
    @FXML
    private TextField keyBInput;
    @FXML
    private TextField alphabetInput;
    @FXML
    private TextField workInput;
    @FXML
    private TextField resultInput;

    @FXML
    public void onDecrypt(MouseEvent event) {
        if (checkInputLength())
            return;

        final String decrypted = cipher.decrypt(
                alphabetInput.getText(),
                workInput.getText(),
                Integer.parseInt(keyBInput.getText()),
                Integer.parseInt(keyTInput.getText())
        );

        resultInput.setText(decrypted);
    }

    @FXML
    public void onEncrypt(MouseEvent event) {
        if (checkInputLength())
            return;

        final String encrypted = cipher.encrypt(
                alphabetInput.getText(),
                workInput.getText(),
                Integer.parseInt(keyBInput.getText()),
                Integer.parseInt(keyTInput.getText())
        );

        resultInput.setText(encrypted);
    }

    @FXML
    public void onFileLoad(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open encrypted text");
        fileChooser.setSelectedExtensionFilter(
                new FileChooser.ExtensionFilter("Encrypted text", ".txt")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file == null) return;

        final String content = StringUtil.readFromFile(file);
        workInput.setText(content);
    }

    @FXML
    public void onFileSave(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Encrypted text", ".txt")
        );
        fileChooser.setTitle("Save encrypted text");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            StringUtil.saveToFile(resultInput.getText(), file);
        }
    }

    @FXML
    public void initialize() {
        keyTInput.textProperty().addListener(FxUtil.makeFilter(keyTInput, true, -1));
        keyBInput.textProperty().addListener(FxUtil.makeFilter(keyBInput, true, -1));
    }

    private boolean checkInputLength() {
        final String tText = keyTInput.getText();
        if (tText.isEmpty()) {
            showError("Ключ T не повинен бути пустим");
            return true;
        }

        final String bText = keyBInput.getText();
        if (bText.isEmpty()) {
            showError("Ключ B не повинен бути пустим");
            return true;
        }

        if (alphabetInput.getText().isEmpty()) {
            showError("Алфавіт не повинен бути пустим");
            return true;
        }

        final int t = Integer.parseInt(tText);
        final int b = Integer.parseInt(bText);
        if (NumberUtil.gcd(t,b) != 1) {
            showWarn("Найбільший спільний дільник НЕ дорівнює одиниці,\n" +
                    "таким чином неможливо гарантувати взаємно однозначне відображення");
        }

        return false;
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).showAndWait();
    }

    private void showWarn(String message) {
        new Alert(Alert.AlertType.WARNING, message, ButtonType.OK).showAndWait();
    }

}