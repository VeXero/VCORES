package io.bootify.vcore.service;

import io.bootify.vcore.domain.CommissionRequest;
import io.bootify.vcore.model.ArtstyleType;
import io.bootify.vcore.model.CommissionRequestDTO;
import io.bootify.vcore.model.CommissionType;
import io.bootify.vcore.repos.CommissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommissionServiceTest {

    private CommissionRepository commissionRepository;
    private JavaMailSender mailSender;
    private CommissionService commissionService;

    @BeforeEach
    public void setup() {
        commissionRepository = mock(CommissionRepository.class);
        commissionService = new CommissionService(commissionRepository);
    }

    @Test
    public void testCreateCommission() {
        CommissionRequestDTO dto = new CommissionRequestDTO();
        dto.setSenderName("Test User");
        dto.setSenderEmail("test@example.com");
        dto.setCommissionType(CommissionType.HALFBODY);
        dto.setArtstyleType(ArtstyleType.SKETCH);
        dto.setAdditionalNotes("Test notes");

        String result = commissionService.createCommission(dto);

        verify(commissionRepository, times(1)).save(any(CommissionRequest.class));


        assertEquals("Commission submitted successfully!", result);
    }

    @Test
    public void testGetAllCommissions() {
        CommissionRequest entity = new CommissionRequest("User", "u@e.com", CommissionType.HEADSHOT, ArtstyleType.SKETCH, "hi");
        when(commissionRepository.findAll()).thenReturn(Collections.singletonList(entity));

        List<CommissionRequestDTO> result = commissionService.getAllCommissions();

        assertEquals(1, result.size());
        assertEquals("User", result.get(0).getSenderName());
        assertEquals(CommissionType.HEADSHOT, result.get(0).getCommissionType());
        assertEquals(ArtstyleType.SKETCH, result.get(0).getArtstyleType());
    }
}
