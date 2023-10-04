package com.lpt.spring_jms.controllers;

import com.lpt.spring_jms.dto.EmailAttachmentDto;
import com.lpt.spring_jms.dto.EmailDto;
import com.lpt.spring_jms.handlers.FileHandle;
import com.lpt.spring_jms.payloads.response.DataResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping(path = "/api/v1/email")
public class EmailController {
    @Autowired
    private JavaMailSender javaMailSender;
    @PostMapping("/send-email")
    public DataResponse sendEmail(@RequestBody EmailDto emailDto){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailDto.getTo());
        simpleMailMessage.setSubject(emailDto.getSubject());
        simpleMailMessage.setText(emailDto.getText());
        javaMailSender.send(simpleMailMessage);
        return new DataResponse("200","Email send successfully");
    }
    @PostMapping("/send-email-attachment")
    //@ModelAttribute to use link data from request to Java Object or model attribute
    public DataResponse sendEmailAttachment(@ModelAttribute EmailAttachmentDto emailAttachmentDto) throws MessagingException, IOException {

        MultipartFile attachment = emailAttachmentDto.getAttachment();
        String originalFileName = Objects.requireNonNull(attachment.getOriginalFilename());
        File attachmentFile = FileHandle.convertMultipartToFile(attachment, originalFileName);

        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
        mimeMessageHelper.setTo(emailAttachmentDto.getTo());
        mimeMessageHelper.setSubject(emailAttachmentDto.getSubject());
        mimeMessageHelper.setText(emailAttachmentDto.getText(),true);

        mimeMessageHelper.addAttachment(originalFileName, attachmentFile);

        javaMailSender.send(mimeMailMessage);
        return new DataResponse("200","Email with attachment send successfully");
    }
}

// https://www.youtube.com/watch?v=kzuRwkyv4Ag
