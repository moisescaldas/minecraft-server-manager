package io.github.moisescaldas.core.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.github.moisescaldas.config.FileManagerConfig;
import io.github.moisescaldas.core.dto.MinecraftServerDTO;
import io.github.moisescaldas.core.dto.MinecraftVersionDTO;
import io.github.moisescaldas.core.exceptions.ApplicationServerException;
import io.github.moisescaldas.core.exceptions.BusinessRuleException;
import io.github.moisescaldas.core.integration.jsoup.mcversions.MCVersionsClient;
import io.github.moisescaldas.core.integration.rest.file.donwload.FileDownloadClient;
import io.github.moisescaldas.core.integration.rest.launcher.meta.LauncherMetaClient;
import io.github.moisescaldas.core.mapper.MinecraftVersionMapper;
import io.github.moisescaldas.core.util.MessagesPropertiesUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

@ApplicationScoped
@NoArgsConstructor
public class MinecraftDownloadService {

    private LauncherMetaClient launcherMetaClient;
    private MCVersionsClient mcVersionsClient;
    private FileDownloadClient fileDownloadClient;

    @Inject
    public MinecraftDownloadService(LauncherMetaClient launcherMetaClient, MCVersionsClient mcVersionsClient, FileDownloadClient fileDownloadClient) {
        this.launcherMetaClient = launcherMetaClient;
        this.mcVersionsClient = mcVersionsClient;
        this.fileDownloadClient = fileDownloadClient;
    }

    public List<MinecraftVersionDTO> listarVersoes() {
        var manifest = launcherMetaClient.getVersionManifest();
        return manifest.getVersions().stream().map(MinecraftVersionMapper.INSTANCE::versionTOMinecraftVersionDTO).collect(Collectors.toList());
    }

    public MinecraftServerDTO criarServidor(final String version) {
        String minecraftVersion = Objects.isNull(version) ? launcherMetaClient.getVersionManifest().getLatest().getRelease() : version;
        
        var urlDonwload = mcVersionsClient.getUrlDonwloadString(minecraftVersion);
        var serverFile = fileDownloadClient.downloadFile(urlDonwload, ".jar");
        
        var serverFolder = new File(FileManagerConfig.SERVERS_FOLDER, minecraftVersion.replace(".", "_"));
        if (serverFolder.exists() && (serverFolder.isDirectory() || !FileManagerConfig.deletarDiretorios(serverFolder))) {
            throw new BusinessRuleException(MessagesPropertiesUtil.geString("E0003", minecraftVersion));
        }
        FileManagerConfig.configurarDiretorios(serverFolder);
        var targetServerFile = new File(serverFolder, "server.jar");

        if (!FileManagerConfig.sucessfullIOOperation(() -> Files.move(serverFile.toPath(), targetServerFile.toPath(), StandardCopyOption.ATOMIC_MOVE))) {
            throw new ApplicationServerException(MessagesPropertiesUtil.geString("E0002", targetServerFile.getAbsolutePath()));
        }

        return MinecraftServerDTO.builder().serverName(new File(targetServerFile.getParent()).getName()).build();
    }
}
