package com.homework.store.controller.exception;

public class NotFoundException extends Exception {
    // int code
    public NotFoundException(/*int code ,*/ String message) {
        super(message);
        //this.code = code;
    }
}
