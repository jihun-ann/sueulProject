package com.springboot.sueulproject.service;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.springframework.stereotype.Service;

@Service
public class JasyptServiceImpl implements JasyptService{
    @Override
    public String jasyptEncrypt(String value) {
        StandardPBEStringEncryptor stringEncryptor = new StandardPBEStringEncryptor();
        String key = "sueul-password";
        stringEncryptor.setPassword(key);
        stringEncryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        stringEncryptor.setIvGenerator(new RandomIvGenerator());
        return stringEncryptor.encrypt(value);
    }
    @Override
    public String jasyptDecrypt(String value) {
        StandardPBEStringEncryptor stringEncryptor = new StandardPBEStringEncryptor();
        String key = "sueul-password";
        stringEncryptor.setPassword(key);
        stringEncryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        stringEncryptor.setIvGenerator(new RandomIvGenerator());
        return stringEncryptor.decrypt(value);
    }
}
