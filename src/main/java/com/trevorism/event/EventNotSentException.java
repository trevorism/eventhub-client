package com.trevorism.event;

/**
 * @author tbrooks
 */
public class EventNotSentException extends RuntimeException{
    public EventNotSentException(String message) {
        super(message);
    }
}
