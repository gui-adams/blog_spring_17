package br.com.blogback.blogback.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadDir;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDirPath) {
        this.uploadDir = Paths.get(uploadDirPath);
        try {
            Files.createDirectories(this.uploadDir); // Cria o diretório, se não existir
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório de upload", e);
        }
    }

    public String saveFile(MultipartFile file) throws IOException {
        // Validação do tipo de arquivo
        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/png") &&
                        !contentType.equals("image/jpg") &&
                        !contentType.equals("image/jpeg"))) {
            throw new IOException("Formato de arquivo inválido. Apenas PNG, JPG e JPEG são permitidos.");
        }

        // Gera um nome de arquivo único
        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Salva o arquivo no diretório de upload e retorna o caminho
        Path destinationFile = this.uploadDir.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

        return destinationFile.toString();
    }
}
