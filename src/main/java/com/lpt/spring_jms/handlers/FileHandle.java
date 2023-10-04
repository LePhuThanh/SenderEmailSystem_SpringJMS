package com.lpt.spring_jms.handlers;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileHandle {
    public static File convertMultipartToFile (MultipartFile multipartFile, String fileName) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
        multipartFile.transferTo(convFile);
        return convFile;
    }
}
