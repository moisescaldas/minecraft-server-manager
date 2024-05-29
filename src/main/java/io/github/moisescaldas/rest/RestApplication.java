package io.github.moisescaldas.rest;

import java.util.HashSet;
import java.util.Set;

import io.github.moisescaldas.rest.ws.MinecraftDonwloadWebService;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        var classes = new HashSet<Class<?>>();
        classes.add(MinecraftDonwloadWebService.class);

        return classes;
    }
}