package com.skhu.cloud.dto.error;

public class CustomException extends Exception {

    private String path;

    public CustomException(String path) {
        this.path = path;
    }

    public String getPath(){
        return this.path;
    }
}
