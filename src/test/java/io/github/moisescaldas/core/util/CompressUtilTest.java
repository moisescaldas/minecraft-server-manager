package io.github.moisescaldas.core.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.UUID;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.compressors.CompressorException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.moisescaldas.config.FileManagerConfig;

class CompressUtilTest {
    
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
    void decompressTest() throws IOException, CompressorException, ArchiveException {
        // setup
        var classLoader = getClass().getClassLoader();
        var is = classLoader.getResourceAsStream("top-secret.zip");
        var file = File.createTempFile("top-secret" + UUID.randomUUID().toString(), ".zip");
        var destination = new File("target/minecraft-server-runtime/runner");

        // act
        var out = new FileOutputStream(file);
        out.write(is.readAllBytes());
        out.close();
        is.close();

        FileManagerConfig.configurarDiretorios(destination);
        CompressUtil.extract(file.toPath(), destination.toPath());

        // assert
        assertTrue(destination.list().length > 0);
    }
}
