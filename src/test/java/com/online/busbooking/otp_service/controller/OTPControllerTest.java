package com.online.busbooking.otp_service.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.busbooking.otp_service.DTO.OTPRequest;
import com.online.busbooking.otp_service.DTO.OTPResponse;
import com.online.busbooking.otp_service.DTO.OTPValidateRequest;
import com.online.busbooking.otp_service.DTO.ResendOTPRequest;
import com.online.busbooking.otp_service.exception.GenericException;
import com.online.busbooking.otp_service.service.OTPService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
        when(otpService.generateOTP(any(OTPRequest.class))).thenReturn(otpResponse);

        mockMvc.perform(post("/otp/generateOTP")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(otpRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.otpNumber").value("2133"))
                .andExpect(jsonPath("$.referenceNumber").value("OTP-688803"));

    }

    @Test
    void testValidateOTP() throws Exception {
        when(otpService.validateOTP(any(OTPValidateRequest.class))).thenReturn(otpValidation);
        mockMvc.perform(post("/otp/validateOTP")
                .contentType(MediaType.APPLICATION_JSON)
                .content(otpValidationRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OTP verified successfully"));
    }

    @Test
    void testResendOTP() throws Exception {
        when(otpService.resendOTP(any(ResendOTPRequest.class))).thenReturn(otpResendResponse);

        mockMvc.perform(post("/otp/resendOTP")
                .contentType(MediaType.APPLICATION_JSON)
                .content(otpResendRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OTP resent successfully"))
                .andExpect(jsonPath("$.otpNumber").value("3798"));

    }


    @Test
    void testGenericException() throws Exception {
        GenericException exception = new GenericException("204" , "Incorrect OTP" , HttpStatus.BAD_REQUEST);
        OTPValidateRequest request = new OTPValidateRequest();
        request.setReferenceNumber("OTP-123456");
        request.setOtpNumber("6523");
        String json = objectMapper.writeValueAsString(request);
        doThrow(exception).when(otpService).validateOTP(any());
        mockMvc.perform(post("/otp/validateOTP")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timeStamp").exists())
                .andExpect(jsonPath("$.errorCode").value("204"))
                .andExpect(jsonPath("$.errorMessage").value("Incorrect OTP"));
    }

    @Test
    void testGenericExceptionForOTPExpired() throws Exception {
        GenericException exception = new GenericException();
        exception.setCode("202");
        exception.setMessage("OTP is expired");
        exception.setHttpStatus(HttpStatus.BAD_REQUEST);
        OTPValidateRequest request = new OTPValidateRequest();
        request.setReferenceNumber("OTP-123456");
        request.setOtpNumber("6523");
        String json = objectMapper.writeValueAsString(request);
        doThrow(exception).when(otpService).validateOTP(any());
        mockMvc.perform(post("/otp/validateOTP")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timeStamp").exists())
                .andExpect(jsonPath("$.errorCode").value("202"))
                .andExpect(jsonPath("$.errorMessage").value("OTP is expired"));
    }

    @Test
    void testMethodArgumentNotValidException() throws Exception {
        String invalidJson = """
            {
                
                "emailId": "invalidEmail"
            }
        """;
        mockMvc.perform(post("/otp/generateOTP")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());


    }
}
