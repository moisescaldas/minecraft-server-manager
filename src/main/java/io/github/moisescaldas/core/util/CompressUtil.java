package io.github.moisescaldas.core.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.examples.Archiver;
import org.apache.commons.compress.archivers.examples.Expander;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FilenameUtils;

public final class CompressUtil {

    private CompressUtil() {
        throw new UnsupportedOperationException("Nenhuma instancia de CompressUtil para você");
    }

    public static void compress(Path directory, Path destination)
            throws IOException, CompressorException, ArchiveException {
        String compressionFormat = FileNameUtils.getExtension(destination);
        String archiveFormat = FilenameUtils.getExtension(
                destination.getFileName().toString().replace("." + compressionFormat, ""));

        try (OutputStream archive = Files.newOutputStream(destination);
                BufferedOutputStream archiveBuffer = new BufferedOutputStream(archive);
                CompressorOutputStream compressor = new CompressorStreamFactory()
                        .createCompressorOutputStream(compressionFormat, archiveBuffer);
                ArchiveOutputStream<?> archiver = new ArchiveStreamFactory()
                        .createArchiveOutputStream(archiveFormat, compressor)) {
            new Archiver().create(archiver, directory);
        }
    }

    public static void extract(Path archive, Path destination) throws IOException, ArchiveException {
        new Expander().expand(archive, destination);
    }
}
