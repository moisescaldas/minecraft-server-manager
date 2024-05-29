package io.github.moisescaldas.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import io.github.moisescaldas.core.dto.MinecraftVersionDTO;
import io.github.moisescaldas.core.integration.rest.launcher.meta.dto.VersionDTO;

@Mapper
public interface MinecraftVersionMapper {

    MinecraftVersionMapper INSTANCE = Mappers.getMapper(MinecraftVersionMapper.class);

    @Mapping(target = "version", source = "id")
    MinecraftVersionDTO versionTOMinecraftVersionDTO(VersionDTO dto);
}
