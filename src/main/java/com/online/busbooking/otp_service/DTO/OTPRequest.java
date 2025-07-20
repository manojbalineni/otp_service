package com.online.busbooking.otp_service.DTO;

public class OTPRequest {
    private String emailId;
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
