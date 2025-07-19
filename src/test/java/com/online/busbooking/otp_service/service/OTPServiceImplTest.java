package com.online.busbooking.otp_service.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.busbooking.otp_service.DTO.OTPRequest;
import com.online.busbooking.otp_service.DTO.OTPResponse;
import com.online.busbooking.otp_service.DTO.OTPValidateRequest;
import com.online.busbooking.otp_service.DTO.ResendOTPRequest;
import com.online.busbooking.otp_service.entity.OTPEntity;
import com.online.busbooking.otp_service.exception.GenericException;
import com.online.busbooking.otp_service.repository.OTPRepository;
import com.online.busbooking.otp_service.service.impl.OTPServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.File;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OTPServiceImplTest {

    @InjectMocks
    private OTPServiceImpl otpService ;

    @Mock
    private OTPRepository otpRepository;



    private OTPRequest otpRequest;
    private OTPValidateRequest otpValidateRequest;
    private ResendOTPRequest resendOTPRequest;

    @BeforeEach
    void setup() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        otpRequest = objectMapper.readValue(new File("src/test/resources/json/generate_otp_request.json") , OTPRequest.class);
        otpValidateRequest = objectMapper.readValue(new File("src/test/resources/json/otp_validation_req.json") , OTPValidateRequest.class);
        resendOTPRequest = objectMapper.readValue(new File("src/test/resources/json/otp_resend_req.json") , ResendOTPRequest.class);
    }

    @Test
    public void testGenerateOTP(){
        OTPResponse response = otpService.generateOTP(otpRequest);
        assertNotNull(response.getOtpNumber());
        assertEquals("OTP generated successfully" , response.getMessage());
        verify(otpRepository).save(any());
    }
    @Test
    public void testValidateOTP(){
        OTPEntity otpEntity = new OTPEntity();
        otpEntity.setId(1);
        otpEntity.setAttemptCount(0);
        otpEntity.setResendCount(0);
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpEntity.setMemberId("TEST001");
        otpEntity.setCreatedTime(LocalDateTime.now());
        otpEntity.setOtpNumber("5805");
        otpEntity.setReferenceNumber(otpValidateRequest.getReferenceNumber());
        otpEntity.setEmailId("testing@gmail.com");

        when(otpRepository.findByReferenceNumber(otpValidateRequest.getReferenceNumber())).thenReturn(otpEntity);
        OTPResponse response = otpService.validateOTP(otpValidateRequest);
        assertEquals("OTP verified successfully" , response.getMessage() );

    }

    @Test
    public void shouldThrowException_otpDetailsNull(){
        OTPValidateRequest request = new OTPValidateRequest();
        request.setOtpNumber("123");
        request.setReferenceNumber("OTP-123456");
        when(otpRepository.findByReferenceNumber(request.getReferenceNumber())).thenReturn(null);
        GenericException exception = assertThrows(GenericException.class , () -> otpService.validateOTP(request));
        assertEquals("200", exception.getCode());
        assertEquals("OTP Details Not Found", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void shouldThrowException_otpDetailsVerified(){
        OTPValidateRequest request = new OTPValidateRequest();
        request.setOtpNumber("123");
        request.setReferenceNumber("OTP-123456");
        OTPEntity otpEntity = new OTPEntity();
        otpEntity.setVerified(Boolean.TRUE);
        when(otpRepository.findByReferenceNumber(request.getReferenceNumber())).thenReturn(otpEntity);
        GenericException exception = assertThrows(GenericException.class , () -> otpService.validateOTP(request));
        assertEquals("201", exception.getCode());
        assertEquals("OTP Already verified", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void shouldThrowException_otpDetailsExpired(){
        OTPValidateRequest request = new OTPValidateRequest();
        request.setOtpNumber("123");
        request.setReferenceNumber("OTP-123456");
        OTPEntity otpEntity = new OTPEntity();
        otpEntity.setVerified(Boolean.FALSE);
        otpEntity.setCreatedTime(LocalDateTime.now());
        otpEntity.setExpiryTime(LocalDateTime.now().minusMinutes(2));
        when(otpRepository.findByReferenceNumber(request.getReferenceNumber())).thenReturn(otpEntity);
        GenericException exception = assertThrows(GenericException.class , () -> otpService.validateOTP(request));
        assertEquals("202", exception.getCode());
        assertEquals("OTP is expired", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void shouldThrowException_manyFailedAttempts(){
        OTPValidateRequest request = new OTPValidateRequest();
        request.setOtpNumber("123");
        request.setReferenceNumber("OTP-123456");
        OTPEntity otpEntity = new OTPEntity();
        otpEntity.setVerified(Boolean.FALSE);
        otpEntity.setCreatedTime(LocalDateTime.now());
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpEntity.setAttemptCount(6);
        when(otpRepository.findByReferenceNumber(request.getReferenceNumber())).thenReturn(otpEntity);
        GenericException exception = assertThrows(GenericException.class , () -> otpService.validateOTP(request));
        assertEquals("203", exception.getCode());
        assertEquals("Too Many failed Attempts", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void shouldThrowException_wrongOTP(){
        OTPValidateRequest request = new OTPValidateRequest();
        request.setOtpNumber("123");
        request.setReferenceNumber("OTP-123456");
        OTPEntity otpEntity = new OTPEntity();
        otpEntity.setVerified(Boolean.FALSE);
        otpEntity.setCreatedTime(LocalDateTime.now());
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpEntity.setAttemptCount(4);
        otpEntity.setOtpNumber("12345");
        when(otpRepository.findByReferenceNumber(request.getReferenceNumber())).thenReturn(otpEntity);
        GenericException exception = assertThrows(GenericException.class , () -> otpService.validateOTP(request));
        assertEquals("204", exception.getCode());
        assertEquals("Incorrect OTP", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }
    
    @Test
    public void shouldThrowException_ReferenceNotFound(){
        when(otpRepository.findByReferenceNumber(resendOTPRequest.getReferenceNumber())).thenReturn(null);
        GenericException exception = assertThrows(GenericException.class, () -> otpService.resendOTP(resendOTPRequest));
        assertEquals("205", exception.getCode());
        assertEquals("In valid reference Number", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        
    }

    @Test
    public  void shouldThrowException_MaxResendCountExceeds(){
        OTPEntity otpEntity = new OTPEntity();
        otpEntity.setResendCount(5);
        when(otpRepository.findByReferenceNumber(resendOTPRequest.getReferenceNumber())).thenReturn(otpEntity);
        GenericException exception = assertThrows(GenericException.class , () -> otpService.resendOTP(resendOTPRequest));
        assertEquals("206", exception.getCode());
        assertEquals("Maximum Resend count exceeded", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());

    }

    @Test
    public void test_resendSuccessCase(){
        OTPEntity otpEntity = new OTPEntity();
        otpEntity.setResendCount(2);
        otpEntity.setOtpNumber("1234");
        when(otpRepository.findByReferenceNumber(resendOTPRequest.getReferenceNumber())).thenReturn(otpEntity);
        OTPResponse response = otpService.resendOTP(resendOTPRequest);
        assertNotNull(response.getOtpNumber());
        assertEquals("OTP resent successfully" , response.getMessage());
        verify(otpRepository).save(any());
    }





}
