package io.github.moisescaldas.rest.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"timestamp", "error", "message", "detail", "path"})
public class ErrorResponseDTO<D extends Serializable> {

    private LocalDateTime timestamp = LocalDateTime.now();
    private String error;
    private String message;
    private D detail;
    private String path;
}
