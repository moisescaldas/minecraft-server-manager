package io.github.moisescaldas.core.integration.rest.launcher.meta;

import java.net.URL;

import io.github.moisescaldas.core.exceptions.ApplicationServerException;
import io.github.moisescaldas.core.integration.rest.AbstractRestBuilder;
import io.github.moisescaldas.core.integration.rest.launcher.meta.dto.VersionManifestDTO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.ResponseProcessingException;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
public class LauncherMetaClient extends AbstractRestBuilder {

    @Resource(lookup = "url/launcherMeta", type = URL.class)
    private URL endpoint;

    @PostConstruct
    public void setup() {
        this.baseUrl = endpoint;
    }

    public VersionManifestDTO getVersionManifest() {
        try {
            return createRequestBuilder("/" + "version_manifest.json", null, null).accept(MediaType.APPLICATION_JSON)
                    .get(VersionManifestDTO.class);
        } catch (ResponseProcessingException ex) {
            throw new ApplicationServerException(ex);
        }
    }
}
