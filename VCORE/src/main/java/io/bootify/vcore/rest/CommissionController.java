package io.bootify.vcore.rest;

import io.bootify.vcore.model.CommissionRequestDTO;
import io.bootify.vcore.service.CommissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commissions")
public class CommissionController {

    private final CommissionService commissionService;

    public CommissionController(CommissionService commissionService) {
        this.commissionService = commissionService;
    }


    @PostMapping
    public ResponseEntity<String> createCommission(@Valid @RequestBody CommissionRequestDTO dto) {
        String confirmationMessage = commissionService.createCommission(dto);
        return new ResponseEntity<>(confirmationMessage, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<CommissionRequestDTO>> getAllCommissions() {
        List<CommissionRequestDTO> commissions = commissionService.getAllCommissions();
        return ResponseEntity.ok(commissions);
    }
}

