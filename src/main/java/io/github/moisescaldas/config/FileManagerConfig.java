package io.github.moisescaldas.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import io.github.moisescaldas.core.interfaces.CheckIOException;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Singleton
@Startup
public class FileManagerConfig {

    public static final File MINECRAFT_SERVER_MANAGER_FOLDER = new File("minecraft-server-manager");
    public static final File RUNTIME_FOLDER = new File(MINECRAFT_SERVER_MANAGER_FOLDER, "runtime");
    public static final File SERVERS_FOLDER = new File(MINECRAFT_SERVER_MANAGER_FOLDER, "servers");

    @PostConstruct
    public void setup() {
        configurarDiretorios(MINECRAFT_SERVER_MANAGER_FOLDER);
        configurarDiretorios(SERVERS_FOLDER);
        configurarDiretorios(RUNTIME_FOLDER);
    }

    public static final void configurarDiretorios(File file) {
        if ((!file.exists()
                || (file.isFile()
                        && sucessfullIOOperation(() -> Files.delete(file.toPath()))))
                && file.mkdirs()) {
            System.out.println("Criado a pasta: " + file.getAbsolutePath());
        } else {
            System.out.println("Já existe a pasta: " + file.getAbsolutePath());
        }
    }

    public static boolean deletarDiretorios(File file) {
        return sucessfullIOOperation(() -> {
            try (var pathStream = Files.walk(file.toPath())) {
                pathStream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            }
        });
    }

    public static final boolean sucessfullIOOperation(CheckIOException executable) {
        try {
            executable.execute();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
