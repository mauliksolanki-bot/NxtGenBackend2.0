package com.WF360.services;

public class WF360BankDetailUpdateWindowException extends RuntimeException {

    public WF360BankDetailUpdateWindowException() {
        super("Bank details cannot be updated from the 25th through the 4th of the following month.");
    }
}
