package io.bootify.vcore.service;

import io.bootify.vcore.domain.CommissionRequest;
import io.bootify.vcore.model.CommissionRequestDTO;
import io.bootify.vcore.repos.CommissionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommissionService {

    private final CommissionRepository commissionRepository;

    // We can inject the site owner's email from application properties, 
    // or fallback to a default value if not set.
    @Value("${app.owner.email:owner@example.com}")
    private String ownerEmail;

    @Value("${app.email.api-key:}")
    private String emailApiKey;

    public CommissionService(CommissionRepository commissionRepository) {
        this.commissionRepository = commissionRepository;
    }

    public String createCommission(CommissionRequestDTO dto) {

        CommissionRequest entity = new CommissionRequest(
                dto.getSenderName(),
                dto.getSenderEmail(),
                dto.getCommissionType(),
                dto.getArtstyleType(),
                dto.getAdditionalNotes()
        );


        commissionRepository.save(entity);

        sendEmailViaApi(ownerEmail, "New Art Commission Request!",
                "You received a new commission request from " + dto.getSenderName() + "!");

        sendEmailViaApi(dto.getSenderEmail(), "Copy of your Art Commission Request",
                "Hello " + dto.getSenderName() + ", thank you for your request!");

        return "Commission submitted successfully!";
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

            // Example JSON payload for an API like Resend (https://resend.com)
            // Note: Most API providers require you to use a verified 'from' domain.
            String jsonBody = String.format("{\"from\":\"onboarding@resend.dev\", \"to\":[\"%s\"], \"subject\":\"%s\", \"text\":\"%s\"}",
                    to, subject, text);

            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
            restTemplate.exchange("https://api.resend.com/emails", HttpMethod.POST, request, String.class);

            System.out.println("Successfully sent email via API!");
        } catch (Exception e) {
            System.err.println("Could not send email via API: " + e.getMessage());
        }
    }
}
