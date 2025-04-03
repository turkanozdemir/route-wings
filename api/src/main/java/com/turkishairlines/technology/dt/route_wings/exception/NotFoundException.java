package com.turkishairlines.technology.dt.route_wings.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entityName, String fieldName, Long id) {
        super(entityName + " not found with " + fieldName + id);
    }

    public NotFoundException(String entityName, String fieldName, String name) {
        super(entityName + " not found with " + fieldName + name);
    }
}
