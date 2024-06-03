package io.github.moisescaldas.config;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.compress.archivers.ArchiveException;
import org.omnifaces.cdi.Eager;

import io.github.moisescaldas.core.exceptions.ApplicationServerException;
import io.github.moisescaldas.core.integration.rest.file.donwload.FileDownloadClient;
import io.github.moisescaldas.core.util.CompressUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.NoArgsConstructor;

@ApplicationScoped
@NoArgsConstructor
@Eager
public class JavaRunnerConfig {

    @Produces
    @Named("javaRunner")
    public static final File JAVA_RUNER = new File(FileManagerConfig.RUNTIME_FOLDER + "/jdk/bin/java.exe");

    @Resource(lookup = "url/javaRunner", type = URL.class)
    private URL javaRunnerDownloadURL;

    @Inject
    private FileDownloadClient fileDownloadClient;

    @Inject
    public JavaRunnerConfig(FileDownloadClient fileDownloadClient) {
        this.fileDownloadClient = fileDownloadClient;
    }

    @PostConstruct
    public void setup() {
        FileManagerConfig.configurarDiretorios(FileManagerConfig.RUNTIME_FOLDER);
        var arquivos = FileManagerConfig.RUNTIME_FOLDER.list();

        if (arquivos.length == 0) {
            try {
                var jdk = fileDownloadClient.downloadFile(javaRunnerDownloadURL.toURI().toString(), null);
                CompressUtil.extract(jdk.toPath(), FileManagerConfig.RUNTIME_FOLDER.toPath());
                var javaRunner = new File( FileManagerConfig.RUNTIME_FOLDER, FileManagerConfig.RUNTIME_FOLDER.list()[0]);
                
                FileManagerConfig.renomearArquivos(javaRunner, new File(FileManagerConfig.RUNTIME_FOLDER, "jdk"));
            } catch (URISyntaxException | IOException | ArchiveException ex) {
                throw new ApplicationServerException(ex);
            }
        }
    }
}
