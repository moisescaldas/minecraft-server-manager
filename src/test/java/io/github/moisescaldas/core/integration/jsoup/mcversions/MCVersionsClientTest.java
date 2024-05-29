package io.github.moisescaldas.core.integration.jsoup.mcversions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MCVersionsClientTest {

    @InjectMocks
    private MCVersionsClient mcVersionsClient;
    
    @Spy
    private URL endpoint;

    {
        try {
            this.endpoint = new URL("https://mcversions.net/download");
        } catch (MalformedURLException e) {
            this.endpoint = null;
        }
    }

    @Test
    void getUrlDonwloadStringTest() {
        // setup
        var minecraftVersion = "1.20.6";

        // act
        var url = mcVersionsClient.getUrlDonwloadString(minecraftVersion);
        System.out.println(url);

        // assert
        assertTrue(StringUtils.isNotBlank(url));
    }
}
