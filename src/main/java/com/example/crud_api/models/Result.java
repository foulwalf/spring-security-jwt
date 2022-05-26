package com.example.crud_api.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@Getter
@Setter
public class Result<T>{
    @Schema(description = "message obtenu")
    private String message;
    @Schema(description = "contenu de la requete")
    private T content;

    public Result(String message, T content) {
        this.message = message;
        this.content = content;
    }
    public Result(String message) {
        this.message = message;
    }
}
