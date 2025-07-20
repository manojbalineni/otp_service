package com.online.busbooking.otp_service.DTO;

public class ResendOTPRequest {

    private String emailId;
    private String referenceNumber;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @Override
    public String toString() {
        return "ResendOTPRequest{" +
                "emailId='" + emailId + '\'' +
                ", referenceNumber='" + referenceNumber + '\'' +
                '}';
    }
}
