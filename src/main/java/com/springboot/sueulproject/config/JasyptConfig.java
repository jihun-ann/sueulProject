package com.springboot.sueulproject.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {
    @Bean("jasyptEncryptorAES")
    public StringEncryptor stringEncryptor(){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        String encryptKey = "sueul-password";

        config.setPassword(encryptKey); // 암호화 키
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256"); // 적용 알고리즘
        config.setKeyObtentionIterations("1000"); // 해싱 반복 횟수
        config.setPoolSize("1"); // 인스턴스 Pool 개수
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // salt 생성
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64"); // 인코딩 방식
        encryptor.setConfig(config);

        return encryptor;
    }
}
