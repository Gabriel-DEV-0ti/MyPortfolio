package com.portfolio.backend.controller;

import com.portfolio.backend.dto.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*")
public class HealthController {

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> healthCheck() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("database", "H2 Embedded Persistent");
        status.put("version", "1.0.0");
        status.put("serverTime", LocalDateTime.now());
        status.put("message", "Backend Java do Portfolio operando normalmente");

        return ResponseEntity.ok(ApiResponseDTO.ok("Serviço ativo", status));
    }
}
