package com.portfolio.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ContactMessageRequestDTO {

    @NotBlank(message = "O campo 'nome' não pode estar vazio.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    private String name;

    @NotBlank(message = "O campo 'empresa' não pode estar vazio.")
    @Size(min = 2, max = 100, message = "A empresa deve ter entre 2 e 100 caracteres.")
    private String company;

    @NotBlank(message = "O campo 'mensagem' não pode estar vazio.")
    @Size(min = 5, max = 2000, message = "A mensagem deve ter entre 5 e 2000 caracteres.")
    private String message;

    public ContactMessageRequestDTO() {
    }

    public ContactMessageRequestDTO(String name, String company, String message) {
        this.name = name;
        this.company = company;
        this.message = message;
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
}
