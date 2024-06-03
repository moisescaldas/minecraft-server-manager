package io.github.moisescaldas.core.integration.rest.file.donwload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import javax.ws.rs.ProcessingException;

import io.github.moisescaldas.core.exceptions.ApplicationServerException;
import io.github.moisescaldas.core.integration.rest.AbstractRestBuilder;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileDownloadClient extends AbstractRestBuilder {

    public File downloadFile(String url, String fileExtension) {
        try {
            this.baseUrl = new URL(url);
            var file = File.createTempFile(UUID.randomUUID().toString(), fileExtension);
            var is = createRequestBuilder(null, null, null).get().readEntity(InputStream.class);
            var writer = new FileOutputStream(file);
            writer.write(is.readAllBytes());

            is.close();
            writer.close();

            return file;
        } catch (IOException | ProcessingException ex) {
            throw new ApplicationServerException(ex);
        }
    }
}
