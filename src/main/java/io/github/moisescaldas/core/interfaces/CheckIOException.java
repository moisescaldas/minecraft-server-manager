package io.github.moisescaldas.core.interfaces;

import java.io.IOException;

@FunctionalInterface
public interface CheckIOException {

    void execute() throws IOException;
}
