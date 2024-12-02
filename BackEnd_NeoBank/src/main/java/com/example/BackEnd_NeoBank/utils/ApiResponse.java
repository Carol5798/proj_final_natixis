package com.example.BackEnd_NeoBank.utils;

import java.util.Map;

public class ApiResponse {
    public boolean success;
    public String message;
    public Map<String, Object> data;

    public ApiResponse(boolean success, String message){
        this.success = success;
        this.message = message;
        this.data = null;
    }

    public ApiResponse(boolean success, String message, Map<String, Object> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
