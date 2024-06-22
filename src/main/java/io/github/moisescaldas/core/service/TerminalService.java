package io.github.moisescaldas.core.util;

import java.io.IOException;

import io.github.moisescaldas.core.exceptions.ApplicationServerException;

public final class TerminalUtil {

    private static final String TERMINAL_INTERFACE = isWindows() ? "cmd.exe /c %s" : "/bin/sh -c %s";

    public TerminalUtil() {
        throw new UnsupportedOperationException("Nenhuma instancia de TerminalUtil para você!");
    }

    public static Process execProcess(String... args) {
        try {
            return Runtime.getRuntime().exec(String.format(TERMINAL_INTERFACE, String.join(" ", args)));
        } catch (IOException e) {
            throw new ApplicationServerException(MessagesPropertiesUtil.getString("E0004"));
        }
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }
}
