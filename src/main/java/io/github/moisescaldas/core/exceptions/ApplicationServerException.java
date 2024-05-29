package io.github.moisescaldas.core.exceptions;

public class ApplicationServerException extends RuntimeException {
    public ApplicationServerException(String msg) {
        super(msg);
    }

    public ApplicationServerException(Throwable ex) {
        super(ex);
    }

    public ApplicationServerException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
