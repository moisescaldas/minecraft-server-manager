package io.github.moisescaldas.core.exceptions;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String msg) {
        super(msg);
    }

    public BusinessRuleException(Throwable ex) {
        super(ex);
    }

    public BusinessRuleException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
