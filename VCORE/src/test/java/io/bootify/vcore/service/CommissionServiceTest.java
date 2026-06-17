package io.bootify.vcore.service;

import io.bootify.vcore.domain.CommissionRequest;
import io.bootify.vcore.model.ArtstyleType;
import io.bootify.vcore.model.CommissionRequestDTO;
import io.bootify.vcore.model.CommissionType;
import io.bootify.vcore.repos.CommissionRepository;
import io.bootify.vcore.rest.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommissionServiceTest {

    private CommissionRepository commissionRepository;
    private JavaMailSender mailSender;
    private SimpMessagingTemplate messagingTemplate;
    private CommissionService commissionService;

    @BeforeEach
    void setup() {
        commissionRepository = mock(CommissionRepository.class);
        mailSender = mock(JavaMailSender.class);
        messagingTemplate = mock(SimpMessagingTemplate.class);

        commissionService = new CommissionService(
                commissionRepository,
                mailSender,
                messagingTemplate
        );
    }

    @Test
    void createCommissionShouldSaveCommissionAndReturnDto() {
        CommissionRequestDTO dto = new CommissionRequestDTO();
        dto.setSenderName("Test User");
        dto.setSenderEmail("test@example.com");
        dto.setCommissionType(CommissionType.HALFBODY);
        dto.setArtstyleType(ArtstyleType.SKETCH);
        dto.setAdditionalNotes("Test notes");

        when(commissionRepository.save(any(CommissionRequest.class)))
                .thenAnswer(invocation -> {
                    CommissionRequest savedCommission = invocation.getArgument(0);
                    savedCommission.setId(1L);
                    return savedCommission;
                });

        CommissionRequestDTO result = commissionService.createCommission(dto);

        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getSenderName());
        assertEquals("test@example.com", result.getSenderEmail());
        assertEquals(CommissionType.HALFBODY, result.getCommissionType());
        assertEquals(ArtstyleType.SKETCH, result.getArtstyleType());
        assertEquals("Test notes", result.getAdditionalNotes());

        verify(commissionRepository, times(1)).save(any(CommissionRequest.class));
    }

    @Test
    void createCommissionShouldSaveCorrectEntityValues() {
        CommissionRequestDTO dto = new CommissionRequestDTO();
        dto.setSenderName("Entity Test");
        dto.setSenderEmail("entity@test.com");
        dto.setCommissionType(CommissionType.FULLBODY);
        dto.setArtstyleType(ArtstyleType.RENDER);
        dto.setAdditionalNotes("Entity notes");

        when(commissionRepository.save(any(CommissionRequest.class)))
                .thenAnswer(invocation -> {
                    CommissionRequest savedCommission = invocation.getArgument(0);
                    savedCommission.setId(2L);
                    return savedCommission;
                });

        commissionService.createCommission(dto);

        ArgumentCaptor<CommissionRequest> captor =
                ArgumentCaptor.forClass(CommissionRequest.class);

        verify(commissionRepository).save(captor.capture());

        CommissionRequest capturedEntity = captor.getValue();

        assertEquals("Entity Test", capturedEntity.getSenderName());
        assertEquals("entity@test.com", capturedEntity.getSenderEmail());
        assertEquals(CommissionType.FULLBODY, capturedEntity.getCommissionType());
        assertEquals(ArtstyleType.RENDER, capturedEntity.getArtstyleType());
        assertEquals("Entity notes", capturedEntity.getAdditionalNotes());
    }

    @Test
    void createCommissionShouldSendWebSocketNotification() {
        CommissionRequestDTO dto = new CommissionRequestDTO();
        dto.setSenderName("Socket User");
        dto.setSenderEmail("socket@test.com");
        dto.setCommissionType(CommissionType.HEADSHOT);
        dto.setArtstyleType(ArtstyleType.SKETCH);
        dto.setAdditionalNotes("WebSocket test");

        when(commissionRepository.save(any(CommissionRequest.class)))
                .thenAnswer(invocation -> {
                    CommissionRequest savedCommission = invocation.getArgument(0);
                    savedCommission.setId(3L);
                    return savedCommission;
                });

        commissionService.createCommission(dto);

        verify(messagingTemplate, times(1))
                .convertAndSend(eq("/topic/commissions"), any(CommissionRequestDTO.class));
    }

    @Test
    void getAllCommissionsShouldReturnCommissionList() {
        CommissionRequest commission = new CommissionRequest(
                "User",
                "user@test.com",
                CommissionType.HEADSHOT,
                ArtstyleType.SKETCH,
                "Notes"
        );

        when(commissionRepository.findAll())
                .thenReturn(List.of(commission));

        List<CommissionRequestDTO> result = commissionService.getAllCommissions();

        assertEquals(1, result.size());
        assertEquals("User", result.get(0).getSenderName());
        assertEquals("user@test.com", result.get(0).getSenderEmail());
        assertEquals(CommissionType.HEADSHOT, result.get(0).getCommissionType());
        assertEquals(ArtstyleType.SKETCH, result.get(0).getArtstyleType());
    }

    @Test
    void getAllCommissionsWithPageableShouldReturnPagedResults() {
        CommissionRequest commission = new CommissionRequest(
                "Paged User",
                "paged@test.com",
                CommissionType.HALFBODY,
                ArtstyleType.SIMPLE,
                "Paged notes"
        );
        commission.setId(5L);

        PageRequest pageable = PageRequest.of(0, 10);

        when(commissionRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(commission), pageable, 1));

        var result = commissionService.getAllCommissions(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(5L, result.getContent().get(0).getId());
        assertEquals("Paged User", result.getContent().get(0).getSenderName());
    }

    @Test
    void getCommissionShouldReturnCommissionWhenFound() {
        CommissionRequest commission = new CommissionRequest(
                "Found User",
                "found@test.com",
                CommissionType.FULLBODY,
                ArtstyleType.PAINTING,
                "Found notes"
        );
        commission.setId(42L);

        when(commissionRepository.findById(42L))
                .thenReturn(Optional.of(commission));

        CommissionRequestDTO result = commissionService.getCommission(42L);

        assertEquals(42L, result.getId());
        assertEquals("Found User", result.getSenderName());
        assertEquals("found@test.com", result.getSenderEmail());
        assertEquals(CommissionType.FULLBODY, result.getCommissionType());
        assertEquals(ArtstyleType.PAINTING, result.getArtstyleType());
    }

    @Test
    void getCommissionShouldThrowResourceNotFoundExceptionWhenMissing() {
        when(commissionRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            commissionService.getCommission(99L);
        });
    }
}