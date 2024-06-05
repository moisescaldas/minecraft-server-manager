package io.github.moisescaldas.rest.ws;

import io.github.moisescaldas.core.service.MinecraftServerService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("servers")
public class MinecraftServerWebService {

    @Inject
    private MinecraftServerService minecraftServerService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response recuperarServidores() {
        var result = minecraftServerService.recuperarServidores();
        return Response.ok(result).build();
    }
    
    @POST
    @Path("{serverName}/start")
    public Response iniciarServidor(@PathParam("serverName") String serverName) {
        minecraftServerService.iniciarServidor(serverName);
        return Response.noContent().build();
    }

    @GET
    @Path("{serverName}/config")
    @Produces(MediaType.APPLICATION_JSON)
    public Response recuperarConfiguracoes(@PathParam("serverName") String serverName) {
        var configuracoes = minecraftServerService.recuperarPropriedades(serverName);
        return Response.ok(configuracoes).build();
    }
}
