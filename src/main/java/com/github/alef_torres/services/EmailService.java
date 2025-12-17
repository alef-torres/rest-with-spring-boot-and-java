package com.github.alef_torres.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alef_torres.config.EmailConfig;
import com.github.alef_torres.data.dto.request.EmailRequestDTO;
import com.github.alef_torres.mail.EmailSender;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailConfig emailConfig;

    public void sendSimpleEmail(EmailRequestDTO emailRequest) throws MessagingException {
        emailSender
                .to(emailRequest.getTo())
                .withSubject(emailRequest.getSubject())
                .withMessage(emailRequest.getBody())
                .send(emailConfig);
    }

    public void setEmailWithAttachment(String emailRequestJson, MultipartFile attachment) throws MessagingException {
        File tempFile = null;
        try {
            EmailRequestDTO emailRequest = new ObjectMapper().readValue(emailRequestJson, EmailRequestDTO.class);
            tempFile = File.createTempFile("attachment", attachment.getOriginalFilename());
            attachment.transferTo(tempFile);

            emailSender
                    .to(emailRequest.getTo())
                    .withSubject(emailRequest.getSubject())
                    .withMessage(emailRequest.getBody())
                    .attach(tempFile.getAbsolutePath())
                    .send(emailConfig);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao processar o Json",e);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar o arquivo",e);
        } finally {
            if (tempFile != null && tempFile.exists()) tempFile.delete();
        }
    }

}
