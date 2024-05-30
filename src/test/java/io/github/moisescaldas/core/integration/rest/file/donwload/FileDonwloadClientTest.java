package io.github.moisescaldas.core.integration.rest.file.donwload;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FileDonwloadClientTest {
    
    @Spy
    private FileDonwloadClient fileDonwloadClient;

    @Test
    void donwloadFileTest() {
       // setup
       var url = "https://piston-data.mojang.com/v1/objects/145ff0858209bcfc164859ba735d4199aafa1eea/server.jar";
       
       // act
        var file = fileDonwloadClient.donwloadFile(url, ".jar");
        System.out.println(file.getAbsolutePath());

       // assert
        assertTrue(file.exists());
    }
}
