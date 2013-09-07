/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.hive.scavengerhunt;

/**
 *
 * @author
 * mstark
 */
public abstract class ReturnMessage {
    private boolean error;
    private String message;

    /**
     * @return the error
     */
    public boolean isError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
