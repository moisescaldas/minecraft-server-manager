package io.github.moisescaldas.core.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public final class MessagesPropertiesUtil {
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages");

    private MessagesPropertiesUtil() {
        throw new UnsupportedOperationException("Nenhuma instancia de MessagesPropertiesUtil para você");
    }

    public static String getString(String key) {
        return BUNDLE.getString(key);
    }

    public static String geString(String key, Object... args) {
        return MessageFormat.format(getString(key), args);
    }
}
