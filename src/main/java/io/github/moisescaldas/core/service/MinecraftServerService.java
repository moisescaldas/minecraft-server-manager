package io.github.moisescaldas.core.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import io.github.moisescaldas.config.FileManagerConfig;
import io.github.moisescaldas.core.dto.MinecraftServerDTO;
import io.github.moisescaldas.core.exceptions.ApplicationServerException;
import io.github.moisescaldas.core.exceptions.ResourceNotFoundException;
import io.github.moisescaldas.core.util.MessagesPropertiesUtil;
import io.github.moisescaldas.core.util.TerminalUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
public class MinecraftServerService {

    @Inject
    @Named("javaRunner")
    private File javaRunner;

    public List<MinecraftServerDTO> recuperarServidores() {
        var servidores = Arrays.asList(FileManagerConfig.SERVERS_FOLDER.list());
        return servidores.stream().map(folderName -> MinecraftServerDTO.builder().serverName(folderName).build())
                .toList();
    }

    public void iniciarServidor(final String serverName) {
        var serverFolder = new File(FileManagerConfig.SERVERS_FOLDER, serverName);
        var eulaFile = new File(serverFolder, "eula.txt");

        if (!serverFolder.exists()) {
            throw new ResourceNotFoundException(MessagesPropertiesUtil.geString("E0005", serverName));
        }

        if (!eulaFile.exists()) {
            criarArquivoEula(eulaFile);
        }

        final var process = TerminalUtil.execProcess("cd", serverFolder.getAbsolutePath(), "&",
                javaRunner.getAbsolutePath(), "-jar", "server.jar", "-nogui");

        // TODO: substituir por um service executor
        new Thread(() -> {
            try (var scanner = new Scanner(process.getInputStream())) {

                while (scanner.hasNext()) {
                    System.out.println(scanner.nextLine());
                }
            }
        }).start();
    }

    private static void criarArquivoEula(File file) {
        FileOutputStream out = null;

        try {
            if (file.createNewFile()) {
                out = new FileOutputStream(file);
                out.write("eula=true".getBytes());
            } else {
                throw new IOException();
            }

            out.close();
        } catch (IOException ex) {
            throw new ApplicationServerException(MessagesPropertiesUtil.geString("E0006", file.getName()), ex);
        }
    }
}
