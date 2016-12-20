package com.cosmin.cartracking.security.Exception;

public class NotLoggedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "User is not logged";
    }
}
