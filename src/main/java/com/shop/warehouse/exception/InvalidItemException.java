package com.shop.warehouse.exception;

public class InvalidItemException extends RuntimeException {

    public InvalidItemException(String msg) {
        super(msg);
    }
}
