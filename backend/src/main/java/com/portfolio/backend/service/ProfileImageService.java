package com.portfolio.backend.service;

import com.portfolio.backend.dto.ImageMetadataDTO;
import com.portfolio.backend.dto.ProfileImageResponseDTO;
import com.portfolio.backend.exception.FileValidationException;
import com.portfolio.backend.exception.ResourceNotFoundException;
import com.portfolio.backend.model.ProfileImage;
import com.portfolio.backend.repository.ProfileImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class ProfileImageService {

    private static final long MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024; // 5 MB
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp",
            "image/gif"
    );

    private final ProfileImageRepository repository;

    @Autowired
    public ProfileImageService(ProfileImageRepository repository) {
        this.repository = repository;
    }

    /**
     * Valida e armazena uma nova foto de perfil no banco de dados com segurança.
     */
    @Transactional
    public ProfileImageResponseDTO saveProfileImage(MultipartFile file, String baseUrl) {
        // 1. Validação de nulidade e conteúdo vazio
        if (file == null || file.isEmpty()) {
            throw new FileValidationException("O arquivo enviado está vazio ou não foi selecionado.");
        }

        // 2. Validação de tamanho (máximo 5MB)
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new FileValidationException("O arquivo excede o limite máximo permitido de 5 MB.");
        }

        // 3. Validação de Content-Type informado
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new FileValidationException("Formato de arquivo não suportado. Permitido apenas JPEG, PNG, WEBP e GIF.");
        }

        // 4. Sanitização do nome do arquivo (prevenção de Path Traversal)
        String originalFilename = file.getOriginalFilename();
        String sanitizedFilename = sanitizeFilename(originalFilename);

        // 5. Validação de Magic Bytes (inspeção de cabeçalho binário real do arquivo)
        try (InputStream is = file.getInputStream()) {
            byte[] header = new byte[12];
            int read = is.read(header);
            if (read < 4 || !isValidImageMagicBytes(header, contentType)) {
                throw new FileValidationException("O conteúdo do arquivo não corresponde a uma imagem válida e segura.");
            }
        } catch (IOException e) {
            throw new FileValidationException("Falha ao ler o conteúdo do arquivo: " + e.getMessage());
        }

        // 6. Leitura dos bytes para persistência em BLOB no banco de dados
        byte[] imageBytes;
        try {
            imageBytes = file.getBytes();
        } catch (IOException e) {
            throw new FileValidationException("Erro ao processar os bytes da imagem: " + e.getMessage());
        }

        // 7. Desativar fotos antigas (mantendo histórico ou versionamento no banco)
        repository.deactivateAll();

        // 8. Criar e salvar nova entidade ProfileImage
        ProfileImage profileImage = new ProfileImage(
                sanitizedFilename,
                contentType,
                file.getSize(),
                imageBytes,
                LocalDateTime.now(),
                true
        );

        ProfileImage saved = repository.save(profileImage);

        String downloadUrl = baseUrl + "/api/profile/image";

        return new ProfileImageResponseDTO(
                saved.getId(),
                saved.getFileName(),
                saved.getContentType(),
                saved.getFileSize(),
                downloadUrl,
                saved.getUploadedAt(),
                "Foto de perfil atualizada e armazenada no banco de dados com sucesso!"
        );
    }

    /**
     * Retorna a foto de perfil atualmente ativa no banco.
     */
    @Transactional(readOnly = true)
    public ProfileImage getActiveProfileImage() {
        return repository.findFirstByActiveTrueOrderByUploadedAtDesc()
                .orElseThrow(() -> new ResourceNotFoundException("Nenhuma foto de perfil cadastrada no banco de dados."));
    }

    /**
     * Retorna os metadados da foto ativa em formato DTO.
     */
    @Transactional(readOnly = true)
    public ImageMetadataDTO getActiveProfileImageMetadata(String baseUrl) {
        ProfileImage active = getActiveProfileImage();
        String downloadUrl = baseUrl + "/api/profile/image";
        return new ImageMetadataDTO(
                active.getId(),
                active.getFileName(),
                active.getContentType(),
                active.getFileSize(),
                active.getUploadedAt(),
                active.isActive(),
                downloadUrl
        );
    }

    /**
     * Remove / desativa a foto ativa.
     */
    @Transactional
    public void deleteActiveProfileImage() {
        repository.deactivateAll();
    }

    /**
     * Sanitiza o nome do arquivo para mitigar vulnerabilidades de Path Traversal.
     */
    private String sanitizeFilename(String filename) {
        if (filename == null || filename.isBlank()) {
            return "perfil_" + System.currentTimeMillis() + ".jpg";
        }
        // Remove caminhos e caracteres perigosos
        String clean = filename.replaceAll("[^a-zA-Z0-9._-]", "_");
        if (clean.length() > 80) {
            clean = clean.substring(clean.length() - 80);
        }
        return clean;
    }

    /**
     * Verifica as assinaturas de bytes mágicos dos principais formatos de imagem.
     */
    private boolean isValidImageMagicBytes(byte[] header, String contentType) {
        // JPEG: FF D8 FF
        if ((header[0] & 0xFF) == 0xFF && (header[1] & 0xFF) == 0xD8 && (header[2] & 0xFF) == 0xFF) {
            return true;
        }
        // PNG: 89 50 4E 47
        if ((header[0] & 0xFF) == 0x89 && header[1] == 0x50 && header[2] == 0x4E && header[3] == 0x47) {
            return true;
        }
        // GIF: 47 49 46 (GIF87a ou GIF89a)
        if (header[0] == 0x47 && header[1] == 0x49 && header[2] == 0x46) {
            return true;
        }
        // WEBP: "RIFF" .... "WEBP"
        if (header[0] == 'R' && header[1] == 'I' && header[2] == 'F' && header[3] == 'F') {
            if (header.length >= 12 && header[8] == 'W' && header[9] == 'E' && header[10] == 'B' && header[11] == 'P') {
                return true;
            }
        }
        return false;
    }
}
