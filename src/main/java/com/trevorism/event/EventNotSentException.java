package com.trevorism.event;

/**
 * @author tbrooks
 */
class EventNotSentException extends RuntimeException{
    EventNotSentException(String message) {
        super(message);
    }
}
