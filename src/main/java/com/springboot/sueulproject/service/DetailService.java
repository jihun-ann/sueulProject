package com.springboot.sueulproject.service;

import org.springframework.web.multipart.MultipartFile;

public interface DetailService {
    public void multipartFileSave(MultipartFile img,Long detailId);
}
