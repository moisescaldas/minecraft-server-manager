package io.github.moisescaldas.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.moisescaldas.core.integration.jsoup.mcversions.MCVersionsClient;
import io.github.moisescaldas.core.integration.rest.launcher.meta.LauncherMetaClient;
import io.github.moisescaldas.core.integration.rest.launcher.meta.dto.VersionDTO;
import io.github.moisescaldas.core.integration.rest.launcher.meta.dto.VersionManifestDTO;

@ExtendWith(MockitoExtension.class)
class MinecraftDonwloadServiceTest {

    @Mock
    private LauncherMetaClient launcherMetaClient;

    @Mock
    private MCVersionsClient mcVersionsClient;

    @InjectMocks
    private MinecraftDonwloadService minecraftDonwloadService = new MinecraftDonwloadService(launcherMetaClient, mcVersionsClient);

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
}
