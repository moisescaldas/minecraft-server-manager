package io.github.moisescaldas.rest.exception.mapper;

import org.jsoup.internal.StringUtil;

import io.github.moisescaldas.core.exceptions.ApplicationServerException;
import io.github.moisescaldas.core.util.MessagesPropertiesUtil;
import io.github.moisescaldas.rest.dto.ErrorResponseDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@RequestScoped
public class ApplicationServerExceptionMapper implements ExceptionMapper<ApplicationServerException> {

    @Inject
    private HttpServletRequest request;

    @Override
    public Response toResponse(ApplicationServerException exception) {
        var dto = new ErrorResponseDTO<>();
        dto.setError(MessagesPropertiesUtil.getString("R0002"));
        dto.setMessage(exception.getMessage());
        dto.setPath(request.getRequestURI()
                + (!StringUtil.isBlank(request.getQueryString()) ? "?" + request.getQueryString() : ""));

        return Response.status(Status.BAD_REQUEST).entity(dto).type(MediaType.APPLICATION_JSON).build();
    }

}
