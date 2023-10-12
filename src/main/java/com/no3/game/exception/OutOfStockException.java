package com.no3.game.exception;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException(String message){
        super(message);
    }

}
