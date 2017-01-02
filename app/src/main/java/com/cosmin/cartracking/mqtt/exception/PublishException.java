package com.cosmin.cartracking.mqtt.exception;

public class PublishException extends RuntimeException {
    public PublishException(Throwable throwable) {
        super(throwable);
    }
}
