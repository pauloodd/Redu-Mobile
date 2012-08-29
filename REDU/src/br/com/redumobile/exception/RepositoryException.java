package br.com.redumobile.exception;

import android.util.Log;

public class RepositoryException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -6282470126821833043L;

    public RepositoryException() {
        super();
    }

    public RepositoryException(String message) {
        super(message);
        Log.e("Redu", message);
    }
}
