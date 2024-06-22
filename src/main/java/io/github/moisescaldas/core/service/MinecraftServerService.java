package io.github.moisescaldas.core.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import io.github.moisescaldas.config.FileManagerConfig;
import io.github.moisescaldas.core.dto.MinecraftServerDTO;
import io.github.moisescaldas.core.exceptions.ApplicationServerException;
import io.github.moisescaldas.core.exceptions.BusinessRuleException;
import io.github.moisescaldas.core.exceptions.ResourceNotFoundException;
import io.github.moisescaldas.core.util.MessagesPropertiesUtil;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.NotFoundException;
import lombok.NoArgsConstructor;

@ApplicationScoped
@NoArgsConstructor
public class MinecraftServerService {
    
    @Resource(lookup = "concurrent/executorService", type = ManagedExecutorService.class)
    private ManagedExecutorService executorService;
    
    private File javaRunner;
    
    private TerminalService terminalService;
    
    private Map<String, Process> runningServers = new HashMap<>(); 
   
    @Inject
    public MinecraftServerService(@Named("javaRunner") File javaRunner, TerminalService terminalService) {
        this.javaRunner = javaRunner;
        this.terminalService = terminalService;
    }

    public List<MinecraftServerDTO> recuperarServidores() {
        var servidores = Arrays.asList(FileManagerConfig.SERVERS_FOLDER.list());
        return servidores.stream().map(folderName -> MinecraftServerDTO.builder().serverName(folderName).build())
                .toList();
    }

    public void iniciarServidor(final String serverName) {
        var serverFolder = getServerFolder(serverName);
        var eulaFile = new File(serverFolder, "eula.txt");

        if (Objects.nonNull(runningServers.get(serverName)) && runningServers.get(serverName).isAlive()) {
            throw new BusinessRuleException(MessagesPropertiesUtil.geString("E0009", serverName));
        }

        try {
            runningServers.put(serverName, executorService.submit(() -> {
                if (!eulaFile.exists()) {
                    final var process = terminalService.execProcess("cd", serverFolder.getAbsolutePath(), "&", javaRunner.getAbsolutePath(), "-jar", "server.jar", "-nogui");
                    try {
                        process.waitFor();
                        criarArquivoEula(eulaFile);
                    } catch (InterruptedException ex) {
                        throw new ApplicationServerException(ex);
                    }
                } 
                return terminalService.execProcess("cd", serverFolder.getAbsolutePath(), "&", javaRunner.getAbsolutePath(), "-jar", "server.jar", "-nogui");
            }).get());
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationServerException(e);
        }
    }

    public void pararServidor(final String serverName) {
        var process = runningServers.remove(serverName);

        if (Objects.isNull(process)) {
            throw new ResourceNotFoundException(MessagesPropertiesUtil.geString("E0010", serverName));
        }

        terminalService.execProcess(TerminalService.TASKKILL_COMMAND, Long.toString(process.pid()));
    }

    private File getServerFolder(final String serverName) {
        var serverFolder = new File(FileManagerConfig.SERVERS_FOLDER, serverName);

        if (!serverFolder.exists()) {
            throw new ResourceNotFoundException(MessagesPropertiesUtil.geString("E0005", serverName));
        }
        return serverFolder;
    }

    public Map<String, Object> recuperarPropriedades(final String serverName) {
        try {
            var serverFolder = getServerFolder(serverName);
            var propertiesFIS = new FileInputStream(new File(serverFolder, "server.properties"));

            var properties = new Properties();
            properties.load(propertiesFIS);

            var resultado = new HashMap<String, Object>();
            properties.entrySet().stream().forEach(entry -> resultado.put(entry.getKey().toString(), entry.getValue()));

            propertiesFIS.close();
            return resultado;
        } catch (FileNotFoundException e) {
            throw new NotFoundException(MessagesPropertiesUtil.geString("E0007", serverName), e);
        } catch (IOException e) {
            throw new ApplicationServerException(e);
        }
    }

    public void atualizarPropriedades(final String serverName, final Map<String, Object> properties) {
        var serverFolder = getServerFolder(serverName);
        var propertiesFile = new File(serverFolder, "server.properties");
        FileInputStream propertiesFIS = null;
        FileWriter propertiesFOUT = null;
        try {
            propertiesFIS = new FileInputStream(propertiesFile);
            var serverProperties = new Properties();
            serverProperties.load(propertiesFIS);
            propertiesFIS.close();

            properties.entrySet().stream().forEach(entry -> {
                if (Objects.isNull(serverProperties.getProperty(entry.getKey()))) {
                    throw new BusinessRuleException(MessagesPropertiesUtil.geString("E0008", entry.getKey()));
                }

                serverProperties.put(entry.getKey(), entry.getValue());
            });

            propertiesFOUT = new FileWriter(propertiesFile);
            serverProperties.store(propertiesFOUT, serverName);
            propertiesFOUT.close();
            System.out.println(
                    "Configuração do servidor " + serverName + "atualizada: " + propertiesFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            throw new NotFoundException(MessagesPropertiesUtil.geString("E0007", serverName), e);
        } catch (IOException e) {
            throw new ApplicationServerException(e);
        } finally {
            if (Objects.nonNull(propertiesFIS))
                FileManagerConfig.sucessfullIOOperation(propertiesFIS::close);
            if (Objects.nonNull(propertiesFOUT))
                FileManagerConfig.sucessfullIOOperation(propertiesFOUT::close);
        }
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
