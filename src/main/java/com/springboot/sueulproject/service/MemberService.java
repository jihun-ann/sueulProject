package com.springboot.sueulproject.service;

import com.springboot.sueulproject.entity.Member;

import javax.servlet.http.Cookie;

public interface MemberService {

    Cookie cookieSet(String memberId, String check);
    Cookie cookieRemove();

    Member naverLogin(String code);
}
