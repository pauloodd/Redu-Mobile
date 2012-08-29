package br.com.redumobile.exception;

import android.util.Log;

public class FacadeException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -5627298278947792277L;

    public FacadeException(String message) {
        super(message);
        Log.e("Redu", message);
    }

    public FacadeException() {
        super();
    }
}
