package sample;

import javafx.scene.control.TextInputControl;

public final class InputControlUtils {
    private static final String MAX_TEXT_LIMIT = "max-text-limit";

    public static void setMaxTextLimit(TextInputControl control, int maxLength) {
        control.getProperties().put(MAX_TEXT_LIMIT, maxLength);
        control.textProperty().addListener((ov, oldValue, newValue) -> {
            if (control.getText().length() > maxLength) {
                String s = control.getText().substring(0, maxLength);
                control.setText(s);
            }
        });
    }

    public static int getMaxTextLimit(TextInputControl control) {
        Object value = control.getProperties().get(MAX_TEXT_LIMIT);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return -1;
    }
}
