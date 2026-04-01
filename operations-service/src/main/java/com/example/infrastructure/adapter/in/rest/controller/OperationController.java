package com.example.infrastructure.adapter.in.rest.controller;

import com.example.application.port.in.ProcessOperationUseCase;
import com.example.infrastructure.adapter.in.rest.dto.OperationRequest;
import com.example.infrastructure.adapter.in.rest.dto.OperationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para procesar operaciones de tarjetas.
 */
@RestController
@RequestMapping("/api/v1/operations")
@RequiredArgsConstructor
public class OperationController {

    private final ProcessOperationUseCase processOperationUseCase;

    /**
     * POST /api/v1/operations
     * Procesa una operación de consumo o pago en una tarjeta.
     */
    @PostMapping
    public ResponseEntity<OperationResponse> processOperation(
            @Valid @RequestBody OperationRequest request) {
        
        OperationResponse response = processOperationUseCase.processOperation(
                request.cardId(),
                request.amount(),
                request.operation()
        );
        
        return ResponseEntity.ok(response);
    }
}