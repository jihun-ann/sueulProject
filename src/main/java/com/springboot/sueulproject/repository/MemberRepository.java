package com.springboot.sueulproject.repository;

import com.springboot.sueulproject.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MemberRepository extends JpaRepository<Member,String> {

    @Query("select m from Member m where m.nickname = :membernn")
    public Member findByuserNickname(String membernn);

    @Query("select m from Member m where m.memberSsn = :memberSsn")
    public Member findBymemberSsn(String memberSsn);

    @Query("select m from Member m where m.memberPhoneNum =:memberPn")
    public Member findBymemberPn(String memberPn);

    @Query("select m from Member m where m.memberId =:memberId and m.memberPw =:memberPw")
    public Member findBymemberIdPw(String memberId, String memberPw);

    @Modifying
    @Transactional
    @Query("update Member m set m.memberPw = :memberPw where m.memberId=:memberId")
    public int memberPwUpdate(String memberId, String memberPw);

    @Modifying
    @Transactional
    @Query("update Member m set m.nickname = :nickname where m.memberId=:memberId")
    public int memberNnUpdate(String memberId, String nickname);

    @Modifying
    @Transactional
    @Query("update Member m set m.memberName =:memberName where m.memberId =:memberId")
    public int memberNameUpdate(String memberId, String memberName);

    @Modifying
    @Transactional
    @Query("update Member m set m.memberPhoneNum =:memberPhoneNum where m.memberId =:memberId")
    public int memberPnUpdate(String memberId, String memberPhoneNum);


}
