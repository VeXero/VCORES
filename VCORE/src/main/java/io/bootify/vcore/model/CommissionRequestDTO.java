package io.bootify.vcore.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CommissionRequestDTO {

    @NotBlank(message = "Name is required")
    private String senderName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String senderEmail;

    @NotNull(message = "Commission type must be selected (HEADSHOT, HALFBODY, or FULLBODY)")
    private CommissionType commissionType;
    @NotNull(message = "Style type must be selected (SKETCH, SIMPLE, RENDER or PAINTING)")
    private ArtstyleType artstyleType;

    private String additionalNotes;

    // Getters and Setters
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }

    public CommissionType getCommissionType() { return commissionType; }
    public void setCommissionType(CommissionType commissionType) { this.commissionType = commissionType; }

    public ArtstyleType getArtstyleType() { return artstyleType; }
    public void setArtstyleType(ArtstyleType artstyleType) { this.artstyleType = artstyleType; }

    public String getAdditionalNotes() { return additionalNotes; }
    public void setAdditionalNotes(String additionalNotes) { this.additionalNotes = additionalNotes; }
}
