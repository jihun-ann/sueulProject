package com.springboot.sueulproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.sueulproject.entity.Member;
import com.springboot.sueulproject.profile.NaverToken;
import com.springboot.sueulproject.profile.NaverUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;

@Service
public class MemberServiceImpl implements MemberService{
    @Override
    public Cookie cookieSet(String memberId, String check){
        Cookie cookie = new Cookie("memberId", memberId);
        cookie.setPath("/");
        if (check.equals("on")) {
            cookie.setMaxAge(60 * 60 * 24 * 7);
        } else {
            cookie.setMaxAge(60 * 60 * 24);
        }
        return cookie;
    }

    @Override
    public Cookie cookieRemove() {
        Cookie cookie = new Cookie("memberId",null);
        cookie.setMaxAge(0);
        return cookie;
    }

    @Override
    public Member naverLogin(String code) {
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "PECAHnIiw17IlqkcNrm7");
        body.add("client_secret", "MET2Ze1XgJ");
        body.add("code", code);
        body.add("state", "STATE_STRING");

        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(body, header);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.postForEntity("https://nid.naver.com/oauth2.0/token", naverTokenRequest, String.class);
        ObjectMapper obmapper = new ObjectMapper();
        NaverToken naverProfile;
        try {
            naverProfile = obmapper.readValue(response.getBody(), NaverToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders header2 = new HttpHeaders();
        header2.add("Authorization", "Bearer " + naverProfile.getAccess_token());

        HttpEntity<MultiValueMap<String,String>> naverUserRequest = new HttpEntity<>(header2);
        ResponseEntity<String> result = rt.postForEntity("https://openapi.naver.com/v1/nid/me",naverUserRequest,String.class);

        ObjectMapper obmapper2 = new ObjectMapper();
        NaverUser naverUser;
        try {
            naverUser = obmapper2.readValue(result.getBody(), NaverUser.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        Member member = Member.builder().memberId(naverUser.getResponse().getId())
                .memberName(naverUser.getResponse().getName()).nickname(naverUser.getResponse().getNickname())
                .memberPhoneNum(naverUser.getResponse().getMobile()).build();
        return member;
    }
}
