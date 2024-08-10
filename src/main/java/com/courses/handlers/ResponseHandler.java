package com.courses.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(Object data, String message, HttpStatus status) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", data);
        map.put("message", message);
        map.put("status", status);
        return new ResponseEntity<>(map, status);
    }
}
