package io.bootify.vcore.service;

import io.bootify.vcore.domain.CommissionRequest;
import io.bootify.vcore.model.CommissionRequestDTO;
import io.bootify.vcore.repos.CommissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Service
public class CommissionService {

    private final CommissionRepository commissionRepository;
    private final JavaMailSender javaMailSender;
    private final SimpMessagingTemplate messagingTemplate;

    // We can inject the site owner's email from application properties, 
    // or fallback to a default value if not set.
    @Value("${app.owner.email:owner@example.com}")
    private String ownerEmail;

    @Value("${spring.email.api-key:}")
    private String emailApiKey;

    @Value("${app.email.enabled:true}")
    private boolean emailEnabled;

    @Value("${spring.mail.username:}")
    private String configuredMailUser;

    @Value("${spring.mail.password:}")
    private String configuredMailPassword;

    public CommissionService(CommissionRepository commissionRepository, JavaMailSender javaMailSender) {
        this(commissionRepository, javaMailSender, null);
    }

    @Autowired
    public CommissionService(CommissionRepository commissionRepository, JavaMailSender javaMailSender, SimpMessagingTemplate messagingTemplate) {
        this.commissionRepository = commissionRepository;
        this.javaMailSender = javaMailSender;
        this.messagingTemplate = messagingTemplate;
    }

    public CommissionRequestDTO createCommission(CommissionRequestDTO dto) {

        CommissionRequest entity = new CommissionRequest(
                dto.getSenderName(),
                dto.getSenderEmail(),
                dto.getCommissionType(),
                dto.getArtstyleType(),
                dto.getAdditionalNotes()
        );


        CommissionRequest saved = commissionRepository.save(entity);

        // Broadcast new commission to subscribed WebSocket clients (if present)
        try {
            if (messagingTemplate != null) {
                messagingTemplate.convertAndSend("/topic/commissions", dto);
            }
        } catch (Exception e) {
            System.err.println("Could not send WebSocket notification: " + e.getMessage());
        }

        String details = "Name: " + dto.getSenderName() + "\n" +
                         "Email: " + dto.getSenderEmail() + "\n" +
                         "Commission Type: " + dto.getCommissionType() + "\n" +
                         "Art Style: " + dto.getArtstyleType() + "\n" +
                         "Notes: " + (dto.getAdditionalNotes() != null ? dto.getAdditionalNotes().replace("\"", "\\\"") : "");

        if (emailEnabled) {
            sendEmailViaApi(ownerEmail, "New Art Commission Request!",
                    "You received a new commission request from " + dto.getSenderName() + "!\n\nDetails:\n" + details);

            sendEmailViaGmail(dto.getSenderEmail(), "Copy of your Art Commission Request",
                    "Hello " + dto.getSenderName() + ", thank you for your request!\n\nDetails:\n" + details);
        } else {
            System.out.println("Email sending disabled by app.email.enabled=false; skipping email sends.");
        }

        CommissionRequestDTO result = new CommissionRequestDTO();
        result.setId(saved.getId());
        result.setSenderName(saved.getSenderName());
        result.setSenderEmail(saved.getSenderEmail());
        result.setCommissionType(saved.getCommissionType());
        result.setArtstyleType(saved.getArtstyleType());
        result.setAdditionalNotes(saved.getAdditionalNotes());

        return result;
    }


    public List<CommissionRequestDTO> getAllCommissions() {
        return commissionRepository.findAll().stream().map(entity -> {
            CommissionRequestDTO dto = new CommissionRequestDTO();
            dto.setSenderName(entity.getSenderName());
            dto.setSenderEmail(entity.getSenderEmail());
            dto.setCommissionType(entity.getCommissionType());
            dto.setArtstyleType(entity.getArtstyleType());
            dto.setAdditionalNotes(entity.getAdditionalNotes());
            return dto;
        }).collect(Collectors.toList());
    }

    public Page<CommissionRequestDTO> getAllCommissions(Pageable pageable) {
        var page = commissionRepository.findAll(pageable);
        List<CommissionRequestDTO> dtos = page.getContent().stream().map(entity -> {
            CommissionRequestDTO dto = new CommissionRequestDTO();
            dto.setSenderName(entity.getSenderName());
            dto.setSenderEmail(entity.getSenderEmail());
            dto.setCommissionType(entity.getCommissionType());
            dto.setArtstyleType(entity.getArtstyleType());
            dto.setAdditionalNotes(entity.getAdditionalNotes());
            dto.setId(entity.getId());
            return dto;
        }).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }


    public CommissionRequestDTO getCommission(Long id) {
        var entity = commissionRepository.findById(id).orElseThrow(() -> new io.bootify.vcore.rest.ResourceNotFoundException("Commission not found"));
        CommissionRequestDTO dto = new CommissionRequestDTO();
        dto.setSenderName(entity.getSenderName());
        dto.setSenderEmail(entity.getSenderEmail());
        dto.setCommissionType(entity.getCommissionType());
        dto.setArtstyleType(entity.getArtstyleType());
        dto.setAdditionalNotes(entity.getAdditionalNotes());
        dto.setId(entity.getId());
        return dto;
    }

    private void sendEmailViaApi(String to, String subject, String text) {
        if (emailApiKey == null || emailApiKey.isEmpty()) {
            System.err.println("Skipping email API call - no API key configured.");
            return;
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + emailApiKey);
            headers.set("Content-Type", "application/json");

            String htmlText = text.replace("\n", "<br>");

            String jsonBody = String.format("{\"from\":\"onboarding@resend.dev\", \"to\":[\"%s\"], \"subject\":\"%s\", \"html\":\"%s\"}",
                    to, subject, htmlText);

            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
            restTemplate.exchange("https://api.resend.com/emails", HttpMethod.POST, request, String.class);

            System.out.println("Successfully sent email via API!");
        } catch (Exception e) {
            System.err.println("Could not send email via API: " + e.getMessage());
        }
    }

    private void sendEmailViaGmail(String to, String subject, String text) {
        // Skip attempting SMTP send if mail credentials are not configured
        if (configuredMailUser == null || configuredMailUser.isBlank() || configuredMailPassword == null || configuredMailPassword.isBlank()) {
            System.err.println("Skipping Gmail SMTP send - mail credentials not configured (spring.mail.username / spring.mail.password).");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(ownerEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            javaMailSender.send(message);
            System.out.println("Successfully sent email via Gmail SMTP!");
        } catch (Exception e) {
            System.err.println("Could not send email via Gmail: " + e.getMessage());
        }
    }
}
