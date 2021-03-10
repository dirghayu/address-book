package me.dk.addressbook.exceptions;

public class AlredyExistsException extends RuntimeException {
    public AlredyExistsException(String msg){
        super(msg);
    }
}
