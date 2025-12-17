package com.github.alef_torres.controllers;

import com.github.alef_torres.controllers.docs.EmailControllerDocs;
import com.github.alef_torres.data.dto.request.EmailRequestDTO;
import com.github.alef_torres.services.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/email")
public class EmailController implements EmailControllerDocs {

    @Autowired
    private EmailService emailService;

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequest) {
        try {
            emailService.sendSimpleEmail(emailRequest);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body("Email send successful");
    }

    @PostMapping(value = "/withAttachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<String> sendEmailWithAttachment(
            @RequestParam("emailRequest") String emailRequest,
            @RequestParam("attachment") MultipartFile attachment) {
        try {
            emailService.setEmailWithAttachment(emailRequest, attachment);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao processar requisição na controller",e);
        }
        return new ResponseEntity<>("e-Mail with attachment sent successfully!", HttpStatus.OK);
    }
}
