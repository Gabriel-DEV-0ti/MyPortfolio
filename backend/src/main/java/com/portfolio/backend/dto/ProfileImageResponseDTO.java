package com.portfolio.backend.dto;

import java.time.LocalDateTime;

public class ProfileImageResponseDTO {

    private Long id;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private String imageUrl;
    private LocalDateTime uploadedAt;
    private String message;

    public ProfileImageResponseDTO() {
    }

    public ProfileImageResponseDTO(Long id, String fileName, String contentType, Long fileSize, String imageUrl, LocalDateTime uploadedAt, String message) {
        this.id = id;
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.imageUrl = imageUrl;
        this.uploadedAt = uploadedAt;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
