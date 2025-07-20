package com.online.busbooking.otp_service.DTO;

import jakarta.validation.constraints.Pattern;

public class OTPValidateRequest {
    @Pattern(regexp = "^\\d{4}$" , message = "OTP must be exactly 4 digit")
    private String otpNumber;

    @Pattern(regexp = "^OTP\\d{6}$" , message =  "Reference number must start with OTP and should have 6 digits")
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
