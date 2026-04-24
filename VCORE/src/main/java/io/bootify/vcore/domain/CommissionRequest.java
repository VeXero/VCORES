package io.bootify.vcore.domain;

import io.bootify.vcore.model.ArtstyleType;
import jakarta.persistence.*;
import io.bootify.vcore.model.CommissionType;

@Entity
@Table(name = "commission_requests")
public class CommissionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String senderName;

    @Column(nullable = false)
    private String senderEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommissionType commissionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArtstyleType artstyleType;

    @Column(length = 2000)
    private String additionalNotes;

    // Constructors
    public CommissionRequest() {}

    public CommissionRequest(String senderName, String senderEmail, CommissionType commissionType, ArtstyleType artstyleType, String additionalNotes) {
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.commissionType = commissionType;
        this.artstyleType = artstyleType;
        this.additionalNotes = additionalNotes;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

