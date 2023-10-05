package com.lpt.spring_jms.handlers;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class FileHandle {
    public static File convertMultipartToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName); // temporary folder of Java
        multipartFile.transferTo(convFile);
        return convFile;
    }
}
