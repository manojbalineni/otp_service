package com.online.busbooking.otp_service.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public class ResendOTPRequest {

    @Email(message = "Enter a valid email Id")
    private String emailId;

    @Pattern(regexp = "^OTP\\d{6}$" , message =  "Reference number must start with OTP and have 6 digits")
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
