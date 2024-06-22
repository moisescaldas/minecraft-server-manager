package io.github.moisescaldas.rest;

import java.util.HashSet;
import java.util.Set;

import io.github.moisescaldas.rest.exception.mapper.ApplicationServerExceptionMapper;
import io.github.moisescaldas.rest.exception.mapper.BusinessRuleExceptionMapper;
import io.github.moisescaldas.rest.exception.mapper.ResourceNotFoundExceptionMapper;
import io.github.moisescaldas.rest.ws.MinecraftDownloadWebService;
import io.github.moisescaldas.rest.ws.MinecraftServerWebService;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@ApplicationPath("/api")
@OpenAPIDefinition(
        info = @Info(
                title = "Minecraft Server Manager",
                description = "Serviço WEB que gerencia servidores de minecraft",
                version = "v1")
)
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        var classes = new HashSet<Class<?>>();
        // Recursos
        classes.add(MinecraftDownloadWebService.class);
        classes.add(MinecraftServerWebService.class);

        // ExceptionMapper
        classes.add(ResourceNotFoundExceptionMapper.class);
        classes.add(BusinessRuleExceptionMapper.class);
        classes.add(ApplicationServerExceptionMapper.class);

        return classes;
    }
}