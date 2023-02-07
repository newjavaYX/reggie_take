package com.example.reggie.common;

public class ReggieException extends Exception{

    public ReggieException(){
        super("");
    }
    public ReggieException(String message){
        super(message);
    }
}
