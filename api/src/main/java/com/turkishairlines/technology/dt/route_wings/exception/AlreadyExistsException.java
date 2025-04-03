package com.turkishairlines.technology.dt.route_wings.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String entityName, String fieldName, String value) {
        super(entityName + " already exists with " + fieldName + " = " + value);
    }

    public AlreadyExistsException(String entityName, String... fieldEqualsPairs) {
        super(entityName + " already exists with [" + String.join(", ", fieldEqualsPairs) + "]");
    }
}
