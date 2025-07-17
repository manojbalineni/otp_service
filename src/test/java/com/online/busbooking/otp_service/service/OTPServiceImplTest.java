package com.online.busbooking.otp_service.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.busbooking.otp_service.DTO.OTPRequest;
import com.online.busbooking.otp_service.DTO.OTPResponse;
import com.online.busbooking.otp_service.repository.OTPRepository;
import com.online.busbooking.otp_service.service.impl.OTPServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OTPServiceImplTest {

    @InjectMocks
    private OTPServiceImpl otpService ;

    @Mock
    private OTPRepository otpRepository;



    private OTPRequest otpRequest;

    @BeforeEach
    void setup() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        otpRequest = objectMapper.readValue(new File("src/test/resources/json/generate_otp_request.json") , OTPRequest.class);

    }

    @Test
    public void testGenerateOTP(){

        OTPResponse response = otpService.generateOTP(otpRequest);
        assertNotNull(response.getOtpNumber());
        assertEquals("OTP generated successfully" , response.getMessage());
        verify(otpRepository).save(any());
    }



}
