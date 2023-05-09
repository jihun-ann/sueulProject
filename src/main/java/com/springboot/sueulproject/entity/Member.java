package com.springboot.sueulproject.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "member")
public class Member {
    @Id
    private String memberId;
    private String memberPw;
    private String nickname;
    private String memberName;
    private String memberSsn;
    private String memberPhoneNum;
    private String role;
    private String route;

}
