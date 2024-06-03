package io.github.moisescaldas.core.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.moisescaldas.config.FileManagerConfig;
import io.github.moisescaldas.core.integration.jsoup.mcversions.MCVersionsClient;
import io.github.moisescaldas.core.integration.rest.file.donwload.FileDownloadClient;
import io.github.moisescaldas.core.integration.rest.launcher.meta.LauncherMetaClient;
import io.github.moisescaldas.core.integration.rest.launcher.meta.dto.VersionDTO;
import io.github.moisescaldas.core.integration.rest.launcher.meta.dto.VersionManifestDTO;

@ExtendWith(MockitoExtension.class)
class MinecraftDonwloadServiceTest {

    @Mock
    private LauncherMetaClient launcherMetaClient;

    @Mock
    private MCVersionsClient mcVersionsClient;

    @Mock
    private FileDownloadClient FileDownloadClient;

    @InjectMocks
    private MinecraftDownloadService minecraftDonwloadService = new MinecraftDownloadService(launcherMetaClient,
            mcVersionsClient, FileDownloadClient);

    @BeforeEach
    public void setup() {
        var fileManager = new FileManagerConfig();
        fileManager.setup();
    }

    @AfterEach
    public void teardown() {
        try (var pathStream = Files.walk(FileManagerConfig.MINECRAFT_SERVER_MANAGER_FOLDER.toPath())) {
            pathStream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void listarVersoesTest() {
        // setup
        var manifest = new VersionManifestDTO();
        var versions = new ArrayList<VersionDTO>();
        var version = new VersionDTO("1.0.0", "stable");
        versions.add(version);
        manifest.setVersions(versions);

        Mockito.when(launcherMetaClient.getVersionManifest()).thenReturn(manifest);

        // act
        var result = minecraftDonwloadService.listarVersoes().get(0);

        // assert
        assertEquals(version.getId(), result.getVersion());
    }

    @Test
    void criarServidorTest() throws IOException {
        // setup
        var minecraftVersion = "1.20.6";
        var donwloadUrl = "http://dattebayo.com/server.jar";

        Mockito.when(mcVersionsClient.getUrlDonwloadString(minecraftVersion)).thenReturn(donwloadUrl);
        Mockito.when(FileDownloadClient.downloadFile(donwloadUrl, ".jar")).thenReturn(File.createTempFile("MOCK" + new Random().nextInt(),  ".jar"));

        // act and assert
        assertDoesNotThrow(() -> minecraftDonwloadService.criarServidor(minecraftVersion));
    }

}
