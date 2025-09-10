package com.PostGreSQL.exception;
public class ConflictException extends ApiException {
    public ConflictException(String message) { super(message); }
}