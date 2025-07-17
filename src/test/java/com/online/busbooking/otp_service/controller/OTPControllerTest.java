package com.online.busbooking.otp_service.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.busbooking.otp_service.DTO.OTPRequest;
import com.online.busbooking.otp_service.DTO.OTPResponse;
import com.online.busbooking.otp_service.DTO.OTPValidateRequest;
import com.online.busbooking.otp_service.DTO.ResendOTPRequest;
import com.online.busbooking.otp_service.service.OTPService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OTPController.class)
public class OTPControllerTest {

    @MockitoBean
    private OTPService otpService;

    private String otpRequest;
    private OTPResponse otpResponse;
    private String otpValidationRequest;
    private OTPResponse otpValidation;
    private OTPResponse otpResendResponse;
    private String otpResendRequest;

   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
   private MockMvc mockMvc;


    @BeforeEach
    void setup() throws IOException {
        otpRequest = Files.readString(Paths.get("src/test/resources/json/generate_otp_request.json"));
        otpValidationRequest = Files.readString(Paths.get("src/test/resources/json/otp_validation_req.json"));
        otpResendRequest = Files.readString(Paths.get("src/test/resources/json/otp_resend_req.json"));
        otpResponse = objectMapper.readValue(new File("src/test/resources/json/generate_otp_response.json") , OTPResponse.class);
        otpValidation = objectMapper.readValue(new File("src/test/resources/json/otp_validation_res.json") , OTPResponse.class);
        otpResendResponse = objectMapper.readValue(new File("src/test/resources/json/otp_resend_res.json") , OTPResponse.class);

    }

    @Test
    void testGenerateOTPAPI() throws Exception{
        when(otpService.generateOTP(Mockito.any(OTPRequest.class))).thenReturn(otpResponse);

        mockMvc.perform(post("/otp/generateOTP")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(otpRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.otpNumber").value("2133"))
                .andExpect(jsonPath("$.referenceNumber").value("OTP-688803"));


    }

    @Test
    void testValidateOTP() throws Exception {
        when(otpService.validateOTP(Mockito.any(OTPValidateRequest.class))).thenReturn(otpValidation);
        mockMvc.perform(post("/otp/validateOTP")
                .contentType(MediaType.APPLICATION_JSON)
                .content(otpValidationRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OTP verified successfully"));
    }

    @Test
    void testResendOTP() throws Exception {
        when(otpService.resendOTP(Mockito.any(ResendOTPRequest.class))).thenReturn(otpResendResponse);

        mockMvc.perform(post("/otp/resendOTP")
                .contentType(MediaType.APPLICATION_JSON)
                .content(otpResendRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OTP resent successfully"))
                .andExpect(jsonPath("$.otpNumber").value("3798"));

    }
}
