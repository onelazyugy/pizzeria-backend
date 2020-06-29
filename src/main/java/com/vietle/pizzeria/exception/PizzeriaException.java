package com.vietle.pizzeria.exception;

public class PizzeriaException extends Exception {
    private String message;
    private int statusCd;

    public PizzeriaException() {
        super();
    }

    public PizzeriaException(String message, int statusCd) {
        super(message);
        this.message = message;
        this.statusCd = statusCd;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(int statusCd) {
        this.statusCd = statusCd;
    }
}