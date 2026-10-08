package com.portfolio.backend.controller;

import com.portfolio.backend.dto.ApiResponseDTO;
import com.portfolio.backend.dto.ContactMessageRequestDTO;
import com.portfolio.backend.dto.ContactMessageResponseDTO;
import com.portfolio.backend.service.ContactMessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactController {

    private final ContactMessageService contactService;

    @Autowired
    public ContactController(ContactMessageService contactService) {
        this.contactService = contactService;
    }

    /**
     * Recebe mensagens do formulário de contato e salva no banco de dados.
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<ContactMessageResponseDTO>> submitContact(
            @Valid @RequestBody ContactMessageRequestDTO requestDTO) {

        ContactMessageResponseDTO response = contactService.saveMessage(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Mensagem recebida e gravada com sucesso no banco de dados!", response));
    }

    /**
     * Lista histórico de mensagens registradas.
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ContactMessageResponseDTO>>> getAllContacts() {
        List<ContactMessageResponseDTO> messages = contactService.getAllMessages();
        return ResponseEntity.ok(ApiResponseDTO.ok("Mensagens listadas com sucesso", messages));
    }
}
