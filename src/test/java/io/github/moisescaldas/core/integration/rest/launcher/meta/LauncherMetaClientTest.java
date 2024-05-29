package io.github.moisescaldas.core.integration.rest.launcher.meta;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LauncherMetaClientTest {

    @InjectMocks
    private LauncherMetaClient launcherMetaClient;

    @Spy
    private URL endpoint;

    {
        try {
            this.endpoint = new URL("https://launchermeta.mojang.com/mc/game");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getVersionManifestTest() {
        // act
        var manifest = launcherMetaClient.getVersionManifest();

        // assert
        assertDoesNotThrow(() -> System.out.println(manifest.getLatest()));
    }
}
