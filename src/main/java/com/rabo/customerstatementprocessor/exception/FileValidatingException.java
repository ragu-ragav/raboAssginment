package com.rabo.customerstatementprocessor.exception;

public class FileValidatingException extends RuntimeException {

    private final String fileName;
    private final String message;

    public FileValidatingException(String message , String fileName, String cause){
        super(cause);
        this.fileName = fileName;
        this.message = message;
    }

    @Override
    public String toString() {
        return  message + fileName + super.getMessage();
    }
}
