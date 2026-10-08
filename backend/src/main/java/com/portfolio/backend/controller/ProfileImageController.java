package com.portfolio.backend.controller;

import com.portfolio.backend.dto.ApiResponseDTO;
import com.portfolio.backend.dto.ImageMetadataDTO;
import com.portfolio.backend.dto.ProfileImageResponseDTO;
import com.portfolio.backend.model.ProfileImage;
import com.portfolio.backend.service.ProfileImageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/profile/image")
@CrossOrigin(origins = "*")
public class ProfileImageController {

    private final ProfileImageService profileImageService;

    @Autowired
    public ProfileImageController(ProfileImageService profileImageService) {
        this.profileImageService = profileImageService;
    }

    /**
     * Endpoint para upload seguro da foto de perfil.
     * Salva em banco de dados H2 e retorna DTO com metadados e URL.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<ProfileImageResponseDTO>> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        ProfileImageResponseDTO responseDTO = profileImageService.saveProfileImage(file, baseUrl);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Foto de perfil enviada e salva com sucesso!", responseDTO));
    }

    /**
     * Endpoint que retorna os bytes binários da foto de perfil ativa diretamente para tags <img>.
     */
    @GetMapping
    public ResponseEntity<byte[]> getProfileImage() {
        ProfileImage active = profileImageService.getActiveProfileImage();

        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(active.getContentType());
        } catch (Exception e) {
            mediaType = MediaType.IMAGE_JPEG;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + active.getFileName() + "\"")
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).mustRevalidate())
                .body(active.getImageData());
    }

    /**
     * Endpoint que retorna os metadados da imagem ativa em formato DTO.
     */
    @GetMapping("/metadata")
    public ResponseEntity<ApiResponseDTO<ImageMetadataDTO>> getImageMetadata() {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        ImageMetadataDTO metadata = profileImageService.getActiveProfileImageMetadata(baseUrl);
        return ResponseEntity.ok(ApiResponseDTO.ok("Metadados recuperados com sucesso", metadata));
    }

    /**
     * Endpoint para remover / desativar a foto de perfil ativa.
     */
    @DeleteMapping
    public ResponseEntity<ApiResponseDTO<Void>> deleteProfileImage() {
        profileImageService.deleteActiveProfileImage();
        return ResponseEntity.ok(ApiResponseDTO.ok("Foto de perfil removida com sucesso. Revertendo para padrão.", null));
    }
}
