package io.github.moisescaldas.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;

import org.omnifaces.cdi.Eager;

import io.github.moisescaldas.core.interfaces.CheckIOException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.log4j.Log4j2;

@ApplicationScoped
@Eager
@Log4j2
public class FileManagerConfig {
    // pastas
    public static final File USER_FOLDER = new File(System.getProperty("user.home"));
    public static final File MINECRAFT_SERVER_MANAGER_FOLDER = new File(USER_FOLDER, "minecraft-server-manager");
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
            log.info("Criado a pasta: " + file.getAbsolutePath());
        } else {
            log.warn("Já existe a pasta: " + file.getAbsolutePath());
        }
    }

    public static boolean deletarDiretorios(File file) {
        return sucessfullIOOperation(() -> {
            try (var pathStream = Files.walk(file.toPath())) {
                pathStream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            }
        });
    }

    public static boolean renomearArquivos(File source, File target) {
    	try {
			Files.move(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
			log.info("Pasta {} movida para {}", source.getAbsolutePath(), target.getAbsolutePath());
			return true;
		} catch (IOException e) {
			log.info("Falha ao mover pasta {} para {}", source.getAbsolutePath(), target.getAbsolutePath());
			return false;
		}
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
