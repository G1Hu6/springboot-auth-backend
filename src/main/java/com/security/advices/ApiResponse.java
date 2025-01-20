package com.security.advices;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {

    private LocalDateTime timestamp;

    private T data;

    private ApiError error;

    public ApiResponse(){
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(ApiError error) {
        this();
        this.error = error;
    }

    public ApiResponse(T data) {
        this();
        this.data = data;
    }
}
