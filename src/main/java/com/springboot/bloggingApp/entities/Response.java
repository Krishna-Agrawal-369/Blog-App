package com.springboot.bloggingApp.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private boolean status;
    private int responseCode;
    private String message;
    private Object data;
    private List<String> errorMessages;

    public Response(boolean status, int responseCode, List<String> errorMessages) {
        this.status = status;
        this.responseCode = responseCode;
        this.errorMessages = errorMessages;
    }

    public Response(boolean status, int responseCode, String message, Object data) {
        this.status = status;
        this.responseCode = responseCode;
        this.message = message;
        this.data = data;
    }

    public Response(boolean status, int responseCode, String message) {
        this.status = status;
        this.responseCode = responseCode;
        this.message = message;
    }
}
