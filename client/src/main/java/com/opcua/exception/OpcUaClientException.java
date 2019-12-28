package com.opcua.exception;

/**
 * Created by mj on 2018/1/13.
 *
 */
public class OpcUaClientException extends Exception {

    public OpcUaClientException() {
        super();
    }

    public OpcUaClientException(String message) {
        super(message);
    }

    public OpcUaClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
