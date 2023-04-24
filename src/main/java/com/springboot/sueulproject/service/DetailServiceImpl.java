package com.springboot.sueulproject.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DetailServiceImpl implements DetailService{

    @Value("src/main/resources/static/detail")
    private String uploadPath;

    @Override
    public void multipartFileSave(MultipartFile img,Long detailId) {
        String fileName = uploadPath + File.separator + detailId +".png";
        Path savePath = Paths.get(fileName);

        try {
            img.transferTo(savePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
