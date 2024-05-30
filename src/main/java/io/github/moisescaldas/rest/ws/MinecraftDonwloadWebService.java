package io.github.moisescaldas.rest.ws;

import java.util.Objects;
import java.util.stream.Collectors;

import io.github.moisescaldas.core.service.MinecraftDonwloadService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/donwload")
@RequestScoped
public class MinecraftDonwloadWebService {

    @Inject
    private MinecraftDonwloadService minecraftDonwloadService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarVersoes(@QueryParam("type") final String type) {
        var versoes = minecraftDonwloadService.listarVersoes();
        if (Objects.nonNull(type)) {
            versoes = versoes.stream().filter(versao -> type.equals(versao.getType())).collect(Collectors.toList());
        }

        return Response.ok(versoes).build();
    }

    @POST
    public Response criarServidor(@QueryParam("version") final String version) {
        minecraftDonwloadService.criarServidor(version);
        return Response.noContent().build();
    }
}
