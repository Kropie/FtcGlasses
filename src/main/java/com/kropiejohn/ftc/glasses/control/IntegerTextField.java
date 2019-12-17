package com.kropiejohn.ftc.glasses.control;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class IntegerTextField extends TextField {
    public IntegerTextField() {
        init();
    }

    public IntegerTextField(String text) {
        super(text);
        init();
    }

    private void init() {
        textFormatterProperty().set(new TextFormatter<>(change -> change.getControlNewText().matches("-?[0-9]*") ? change : null));
    }
}
