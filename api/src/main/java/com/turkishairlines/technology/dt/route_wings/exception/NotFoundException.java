package com.turkishairlines.technology.dt.route_wings.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entityName, Long id) {
        super(entityName + " not found with id " + id);
    }

    public NotFoundException(String entityName, String id) {
        super(entityName + " not found with name " + id);
    }
}
