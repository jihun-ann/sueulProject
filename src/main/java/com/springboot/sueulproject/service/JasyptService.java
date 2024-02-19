package com.springboot.sueulproject.service;

import com.springboot.sueulproject.entity.Member;

import javax.servlet.http.Cookie;

public interface JasyptService {

    public String jasyptEncrypt(String value);
    public String jasyptDecrypt(String value);

}
