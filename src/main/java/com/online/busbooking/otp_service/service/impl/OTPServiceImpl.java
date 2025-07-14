package com.online.busbooking.otp_service.service.impl;

import com.online.busbooking.otp_service.DTO.OTPRequest;
import com.online.busbooking.otp_service.DTO.OTPResponse;
import com.online.busbooking.otp_service.DTO.OTPValidateRequest;
import com.online.busbooking.otp_service.DTO.ResendOTPRequest;
import com.online.busbooking.otp_service.entity.OTPEntity;
import com.online.busbooking.otp_service.exception.GenericException;
import com.online.busbooking.otp_service.repository.OTPRepository;
import com.online.busbooking.otp_service.service.OTPService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OTPServiceImpl implements OTPService {

    private final OTPRepository otpRepository;

    public OTPServiceImpl(OTPRepository otpRepository){
        this.otpRepository = otpRepository;
    }

    @Override
    public OTPResponse generateOTP(OTPRequest request) {
        String referenceNumber = generateReferenceNumber();
        OTPResponse response = new OTPResponse();
        response.setReferenceNumber(referenceNumber);
        String otpNumber = generateOTPNumber();
        response.setOtpNumber(otpNumber);

        OTPEntity otpEntity = new OTPEntity();
        otpEntity.setOtpNumber(otpNumber);
        otpEntity.setAttemptCount(0);
        otpEntity.setCreatedTime(LocalDateTime.now());
        otpEntity.setEmailId(request.getEmailId());
        otpEntity.setMemberId(request.getMemberId());
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpEntity.setResendCount(0);
        otpEntity.setReferenceNumber(referenceNumber);
        otpRepository.save(otpEntity);
        response.setSuccess(true);
        response.setMessage("OTP generated successfully");
        return response;

    }

    @Override
    public OTPResponse validateOTP(OTPValidateRequest request) {
        OTPResponse response = new OTPResponse();
        OTPEntity otpDetails = otpRepository.findByReferenceNumber(request.getReferenceNumber());
        if(otpDetails == null){
            throw new GenericException("200" , "OTP Details Not Found" , HttpStatus.BAD_REQUEST);
        }
        if(otpDetails.isVerified()){
            throw new GenericException("201" , "OTP Already verified" , HttpStatus.BAD_REQUEST);
        }
        if(otpDetails.getExpiryTime().isBefore(LocalDateTime.now())){
            throw new GenericException("202" , "OTP is expired" , HttpStatus.BAD_REQUEST);
        }
        if(otpDetails.getAttemptCount() >= 5){
            throw new GenericException("203","Too Many failed Attempts" , HttpStatus.BAD_REQUEST);
        }
        if(!otpDetails.getOtpNumber().equals(request.getOtpNumber())){
            otpDetails.setAttemptCount(otpDetails.getAttemptCount() + 1);
            otpRepository.save(otpDetails);
            throw new GenericException("204" , "Incorrect OTP" , HttpStatus.BAD_REQUEST);
        }
        response.setSuccess(true);
        response.setMessage("OTP verified successfully");
        return response;
    }

    @Override
    public OTPResponse resendOTP(ResendOTPRequest request) {
        OTPResponse response = new OTPResponse();
        OTPEntity otpDetails = otpRepository.findByReferenceNumber(request.getReferenceNumber());
        if(otpDetails == null){
            throw  new GenericException("205", "In valid reference Number" , HttpStatus.BAD_REQUEST);
        }
        if(otpDetails.getResendCount() >= 3){
            throw new GenericException("206" , "Maximum Resend count exceeded" ,HttpStatus.BAD_REQUEST);
        }

        String otp = generateOTPNumber();
        String referenceNumber = generateReferenceNumber();
        response.setOtpNumber(otp);
        response.setReferenceNumber(referenceNumber);
        response.setSuccess(true);
        response.setMessage("OTP resent successfully");
        otpDetails.setResendCount(otpDetails.getResendCount() + 1);
        otpDetails.setCreatedTime(LocalDateTime.now());
        otpDetails.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otpDetails);
        return response;
    }

    private String generateOTPNumber() {
        return String.valueOf(new Random().nextInt(9000) + 1000);
    }

    private String generateReferenceNumber() {
        String reference =  String.valueOf(new Random().nextInt(900000) + 100000);
        return "OTP".concat("-").concat(reference);
    }
}
