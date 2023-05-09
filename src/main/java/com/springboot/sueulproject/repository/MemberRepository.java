package com.springboot.sueulproject.repository;

import com.springboot.sueulproject.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member,String> {

    @Query("select m from member m where m.nickname = :membernn")
    public Member findByuserNickname(String membernn);

    @Query(value = "select * from member m where m.role =:role limit :start,10",nativeQuery = true)
    public List<Member> findByRoleAll(String role,int start);

    @Query("select m from member m where m.memberSsn = :memberSsn")
    public Member findBymemberSsn(String memberSsn);

    @Query("select m from member m where m.memberPhoneNum =:memberPn")
    public Member findBymemberPn(String memberPn);

    @Query("select m from member m where m.memberId =:memberId and m.memberPw =:memberPw")
    public Member findBymemberIdPw(String memberId, String memberPw);

    @Modifying
    @Transactional
    @Query("update member m set m.memberPw = :memberPw where m.memberId=:memberId")
    public int memberPwUpdate(String memberId, String memberPw);

    @Modifying
    @Transactional
    @Query("update member m set m.nickname = :nickname where m.memberId=:memberId")
    public int memberNnUpdate(String memberId, String nickname);

    @Modifying
    @Transactional
    @Query("update member m set m.memberName =:memberName where m.memberId =:memberId")
    public int memberNameUpdate(String memberId, String memberName);

    @Modifying
    @Transactional
    @Query("update member m set m.memberPhoneNum =:memberPhoneNum where m.memberId =:memberId")
    public int memberPnUpdate(String memberId, String memberPhoneNum);


}
