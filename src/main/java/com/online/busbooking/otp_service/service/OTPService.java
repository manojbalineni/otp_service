package com.online.busbooking.otp_service.service;

import com.online.busbooking.otp_service.DTO.OTPRequest;
import com.online.busbooking.otp_service.DTO.OTPResponse;
import com.online.busbooking.otp_service.DTO.OTPValidateRequest;
import com.online.busbooking.otp_service.DTO.ResendOTPRequest;

public interface OTPService {
    public OTPResponse generateOTP(OTPRequest request);

    public OTPResponse validateOTP(OTPValidateRequest request);

    public OTPResponse resendOTP(ResendOTPRequest request);
}
