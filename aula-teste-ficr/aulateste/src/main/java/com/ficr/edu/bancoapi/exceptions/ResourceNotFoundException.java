package com.ficr.edu.bancoapi.exceptions;

import java.io.Serializable;

public class ResourceNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
