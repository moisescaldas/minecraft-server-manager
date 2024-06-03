package io.github.moisescaldas.rest;

import java.util.HashSet;
import java.util.Set;

import io.github.moisescaldas.rest.exception.mapper.BusinessRuleExceptionMapper;
import io.github.moisescaldas.rest.ws.MinecraftDownloadWebService;
import io.github.moisescaldas.rest.ws.MinecraftServerWebService;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        var classes = new HashSet<Class<?>>();
        // Recursos
        classes.add(MinecraftDownloadWebService.class);
        classes.add(MinecraftServerWebService.class);

        // ExceptionMapper
        classes.add(BusinessRuleExceptionMapper.class);

        return classes;
    }
}