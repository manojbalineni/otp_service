package com.online.busbooking.otp_service.DTO;

public class OTPValidateRequest {
    private String otpNumber;
    private String referenceNumber;

    public String getOtpNumber() {
        return otpNumber;
    }

    public void setOtpNumber(String otpNumber) {
        this.otpNumber = otpNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @Override
    public String toString() {
        return "OTPValidateRequest{" +
                "otpNumber='" + otpNumber + '\'' +
                ", referenceNumber='" + referenceNumber + '\'' +
                '}';
    }
}
