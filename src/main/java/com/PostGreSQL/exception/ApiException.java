package com.PostGreSQL.exception;

public abstract class ApiException extends RuntimeException {
    public ApiException(String message) { super(message); }
}

