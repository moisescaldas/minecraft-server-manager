package io.github.moisescaldas.core.exceptions;

public class ResourceNotFoundException extends BusinessRuleException {

    public ResourceNotFoundException(String msg) {
        super(msg);
    }
    
    public ResourceNotFoundException(Throwable ex) {
        super(ex);
    }

    public ResourceNotFoundException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
