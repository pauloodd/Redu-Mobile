package br.com.redumobile.exception;

public class ControllerException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 5284572905984504356L;

    public ControllerException() {
        super();
    }

    public ControllerException(String message) {
        super(message);
    }
}