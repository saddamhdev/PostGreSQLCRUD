package com.PostGreSQL.exception;


public class BadRequestException2 extends ApiException {
    public BadRequestException2(String message) {

        super(message);
        System.out.println("Se");
    }
}
