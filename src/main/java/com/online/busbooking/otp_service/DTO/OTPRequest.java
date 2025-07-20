package com.online.busbooking.otp_service.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public class OTPRequest {

    @Email(message = "Please enter valid email id")
    private String emailId;

    @Pattern(regexp = "^MEM.*$" , message = "Member Id must be start with MEM")
    private String memberId;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "OTPRequest{" +
                "emailId='" + emailId + '\'' +
                ", memberId='" + memberId + '\'' +
                '}';
    }
}
