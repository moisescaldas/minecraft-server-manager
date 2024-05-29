package io.github.moisescaldas.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebServiceResponseDTO<E> {

    private String message;
    private String error;
    private E entity;

    public static <E> WebServiceResponseDTO<E> fromEntity(E entity, String error, String message) {
        var dto = new WebServiceResponseDTO<E>();
        dto.setEntity(entity);
        dto.setError(error);
        dto.setMessage(message);

        return dto;
    }
}
