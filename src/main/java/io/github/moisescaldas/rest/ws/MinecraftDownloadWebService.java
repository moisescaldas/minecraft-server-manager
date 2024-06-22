package io.github.moisescaldas.rest.ws;

import java.util.Objects;
import java.util.stream.Collectors;

import io.github.moisescaldas.core.service.MinecraftDownloadService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/download")
@RequestScoped
@Tag(name = "Download API", description = "Recurso para instalar novos servidores")
public class MinecraftDownloadWebService {

    @Inject
    private MinecraftDownloadService minecraftDonwloadService;

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
    @Produces(MediaType.APPLICATION_JSON)
    public Response criarServidor(@QueryParam("version") final String version) {
        var result = minecraftDonwloadService.criarServidor(version);
        return Response.status(Status.CREATED.getStatusCode()).entity(result).build();
    }
}
