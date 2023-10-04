package com.lpt.spring_jms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailAttachmentDto {
    private String to;
    private String subject;
    private String text;
    private MultipartFile attachment;
}
