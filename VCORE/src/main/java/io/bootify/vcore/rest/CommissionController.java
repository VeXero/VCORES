package io.bootify.vcore.rest;

import io.bootify.vcore.model.CommissionRequestDTO;
import io.bootify.vcore.service.CommissionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/commissions")
public class CommissionController {

    private final CommissionService commissionService;

    public CommissionController(CommissionService commissionService) {
        this.commissionService = commissionService;
    }

    @PostMapping
    public ResponseEntity<CommissionRequestDTO> createCommission(@Valid @RequestBody CommissionRequestDTO dto) {
        CommissionRequestDTO saved = commissionService.createCommission(dto);
        Long id = null;
        try {
            java.lang.reflect.Method m = CommissionRequestDTO.class.getMethod("getId");
            if (m != null) {
                Object val = m.invoke(saved);
                if (val instanceof Long) id = (Long) val;
            }
        } catch (Exception ignored) {}

        URI location = id != null ? URI.create("/api/commissions/" + id) : URI.create("/api/commissions");
        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping
    public ResponseEntity<Page<CommissionRequestDTO>> getAllCommissions(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "20") int size) {
        Page<CommissionRequestDTO> result = commissionService.getAllCommissions(PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommissionRequestDTO> getCommission(@PathVariable Long id) {
        CommissionRequestDTO dto = commissionService.getCommission(id);
        return ResponseEntity.ok(dto);
    }
}

