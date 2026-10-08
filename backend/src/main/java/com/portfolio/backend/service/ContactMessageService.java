package com.portfolio.backend.service;

import com.portfolio.backend.dto.ContactMessageRequestDTO;
import com.portfolio.backend.dto.ContactMessageResponseDTO;
import com.portfolio.backend.model.ContactMessage;
import com.portfolio.backend.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactMessageService {

    private final ContactMessageRepository repository;

    @Autowired
    public ContactMessageService(ContactMessageRepository repository) {
        this.repository = repository;
    }

    /**
     * Salva com segurança uma nova mensagem de recrutador/visitante.
     */
    @Transactional
    public ContactMessageResponseDTO saveMessage(ContactMessageRequestDTO dto) {
        // Sanitização básica contra tags HTML maliciosas (XSS)
        String cleanName = sanitizeHtml(dto.getName());
        String cleanCompany = sanitizeHtml(dto.getCompany());
        String cleanMessage = sanitizeHtml(dto.getMessage());

        ContactMessage entity = new ContactMessage(
                cleanName,
                cleanCompany,
                cleanMessage,
                LocalDateTime.now()
        );

        ContactMessage saved = repository.save(entity);

        return new ContactMessageResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getCompany(),
                saved.getMessage(),
                saved.getReceivedAt(),
                "Mensagem recebida e armazenada com sucesso no banco de dados!"
        );
    }

    /**
     * Lista todas as mensagens recebidas ordenadas pela mais recente.
     */
    @Transactional(readOnly = true)
    public List<ContactMessageResponseDTO> getAllMessages() {
        return repository.findAllByOrderByReceivedAtDesc().stream()
                .map(m -> new ContactMessageResponseDTO(
                        m.getId(),
                        m.getName(),
                        m.getCompany(),
                        m.getMessage(),
                        m.getReceivedAt(),
                        "OK"
                ))
                .collect(Collectors.toList());
    }

    private String sanitizeHtml(String input) {
        if (input == null) return "";
        return input.replace("<", "&lt;").replace(">", "&gt;").trim();
    }
}
