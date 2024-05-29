package io.github.moisescaldas.core.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.github.moisescaldas.core.dto.MinecraftVersionDTO;
import io.github.moisescaldas.core.integration.jsoup.mcversions.MCVersionsClient;
import io.github.moisescaldas.core.integration.rest.launcher.meta.LauncherMetaClient;
import io.github.moisescaldas.core.mapper.MinecraftVersionMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

@ApplicationScoped
@NoArgsConstructor
public class MinecraftDonwloadService {

    private LauncherMetaClient launcherMetaClient;
    private MCVersionsClient mcVersionsClient;

    @Inject
    public MinecraftDonwloadService(final LauncherMetaClient launcherMetaClient, final MCVersionsClient mcVersionsClient) {
        this.launcherMetaClient = launcherMetaClient;
        this.mcVersionsClient = mcVersionsClient;
    }

    public List<MinecraftVersionDTO> listarVersoes() {
        var manifest = launcherMetaClient.getVersionManifest();
        return manifest.getVersions().stream().map(MinecraftVersionMapper.INSTANCE::versionTOMinecraftVersionDTO).collect(Collectors.toList());
    }

    public void criarServidor(final String version) {
        String minecraftVersion = Objects.isNull(version) ? launcherMetaClient.getVersionManifest().getLatest().getRelease() : version;
        var urlDonwload = mcVersionsClient.getUrlDonwloadString(minecraftVersion);
    }
}
