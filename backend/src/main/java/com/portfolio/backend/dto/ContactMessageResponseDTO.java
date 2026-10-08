package com.portfolio.backend.dto;

import java.time.LocalDateTime;

public class ContactMessageResponseDTO {

    private Long id;
    private String name;
    private String company;
    private String message;
    private LocalDateTime receivedAt;
    private String status;

    public ContactMessageResponseDTO() {
    }

    public ContactMessageResponseDTO(Long id, String name, String company, String message, LocalDateTime receivedAt, String status) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.message = message;
        this.receivedAt = receivedAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
