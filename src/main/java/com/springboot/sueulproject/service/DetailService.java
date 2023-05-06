package com.springboot.sueulproject.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface DetailService {
    public void multipartFileSave(MultipartFile img,Long detailId);
    public List<Map.Entry> todayWeather();
}
