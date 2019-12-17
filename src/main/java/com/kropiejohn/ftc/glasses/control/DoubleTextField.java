package com.kropiejohn.ftc.glasses.control;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class DoubleTextField extends TextField {
    public DoubleTextField() {
        init();
    }

    public DoubleTextField(String text) {
        super(text);
        init();
    }

    private void init() {
        textFormatterProperty().set(new TextFormatter<>(change -> change.getControlNewText().matches("-?(\\d*\\.?\\d*)") ? change : null));
    }
}
