package com.online.busbooking.otp_service.controller;

import com.online.busbooking.otp_service.DTO.OTPRequest;
import com.online.busbooking.otp_service.DTO.OTPResponse;
import com.online.busbooking.otp_service.DTO.OTPValidateRequest;
import com.online.busbooking.otp_service.DTO.ResendOTPRequest;
import com.online.busbooking.otp_service.service.OTPService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/otp")
public class OTPController {

    private final OTPService otpService;

    public OTPController(OTPService otpService){
        this.otpService = otpService;
    }

    @PostMapping(value = "/generateOTP")
    public OTPResponse generateOTP(@RequestBody @Valid OTPRequest request){
        return otpService.generateOTP(request);
    }

    @PostMapping(value = "/validateOTP")
    public OTPResponse validateOTO(@RequestBody @Valid OTPValidateRequest request){
        return otpService.validateOTP(request);
    }

    @PostMapping(value = "/resendOTP")
    public OTPResponse resendOTP(@RequestBody @Valid ResendOTPRequest request){
        return otpService.resendOTP(request);
    }

}
