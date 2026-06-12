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
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommissionServiceTest {

    private CommissionRepository commissionRepository;
    private JavaMailSender mailSender;
    private SimpMessagingTemplate messagingTemplate;
    private CommissionService commissionService;

    @BeforeEach
    public void setup() {
        commissionRepository = mock(CommissionRepository.class);
        mailSender = mock(JavaMailSender.class);
        messagingTemplate = mock(SimpMessagingTemplate.class);
        commissionService = new CommissionService(commissionRepository, mailSender, messagingTemplate);
    }

    @Test
    public void testCreateCommission() {
        CommissionRequestDTO dto = new CommissionRequestDTO();
        dto.setSenderName("Test User");
        dto.setSenderEmail("test@example.com");
        dto.setCommissionType(CommissionType.HALFBODY);
        dto.setArtstyleType(ArtstyleType.SKETCH);
        dto.setAdditionalNotes("Test notes");

        when(commissionRepository.save(any(CommissionRequest.class))).thenAnswer(inv -> {
            CommissionRequest arg = inv.getArgument(0);
            arg.setId(1L);
            return arg;
        });

        CommissionRequestDTO result = commissionService.createCommission(dto);

        verify(commissionRepository, times(1)).save(any(CommissionRequest.class));
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/commissions"), any(CommissionRequestDTO.class));

        assertEquals("Test User", result.getSenderName());
        assertEquals(1L, result.getId());
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

    @Test
    public void testGetAllCommissionsWithPageable() {
        CommissionRequest e1 = new CommissionRequest("A", "a@e.com", CommissionType.HEADSHOT, ArtstyleType.SKETCH, "n");
        e1.setId(5L);
        List<CommissionRequest> list = Collections.singletonList(e1);
        Pageable pageable = PageRequest.of(0, 10);
        when(commissionRepository.findAll(pageable)).thenReturn(new PageImpl<>(list, pageable, 1));

        var page = commissionService.getAllCommissions(pageable);

        assertEquals(1, page.getTotalElements());
        assertEquals(1, page.getContent().size());
        assertEquals(5L, page.getContent().get(0).getId());
    }

    @Test
    public void testGetCommissionFound() {
        CommissionRequest entity = new CommissionRequest("Found", "f@e.com", CommissionType.HALFBODY, ArtstyleType.SKETCH, "ok");
        entity.setId(42L);
        when(commissionRepository.findById(42L)).thenReturn(Optional.of(entity));

        CommissionRequestDTO dto = commissionService.getCommission(42L);

        assertEquals(42L, dto.getId());
        assertEquals("Found", dto.getSenderName());
        assertEquals(CommissionType.HALFBODY, dto.getCommissionType());
    }

    @Test
    public void testGetCommissionNotFound() {
        when(commissionRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(io.bootify.vcore.rest.ResourceNotFoundException.class, () -> {
            commissionService.getCommission(99L);
        });
    }
        @Test
        public void testGetCommissionNotFound2() {
            when(commissionRepository.findById(88L)).thenReturn(Optional.empty());
    
            Assertions.assertThrows(io.bootify.vcore.rest.ResourceNotFoundException.class, () -> {
                commissionService.getCommission(88L);
            });
        }
    
            @Test
            public void testGetCommissionNotFound3() {
                when(commissionRepository.findById(67L)).thenReturn(Optional.empty());
        
                Assertions.assertThrows(io.bootify.vcore.rest.ResourceNotFoundException.class, () -> {
                    commissionService.getCommission(67L);
                });
            }
    
}