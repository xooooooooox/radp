package com.x9x.radp.commons.json.jackson.exception;

/**
 * @author x9x
 * @since 2024-09-23 13:54
 */
public class JacksonException extends RuntimeException {

    private static final long serialVersionUID = 9041976807836061564L;

    public JacksonException(Throwable cause) {
        super(cause);
    }
}
