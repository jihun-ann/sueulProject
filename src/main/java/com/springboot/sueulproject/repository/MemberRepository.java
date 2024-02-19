package com.springboot.sueulproject.repository;

import com.springboot.sueulproject.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member,String> {

    @Query(value = "select m.* from member m where m.nickname = :membernn limit 1",nativeQuery = true)
    public Member findByuserNickname(@Param("membernn") String membernn);

    @Query(value = "select m.* from member m where m.role =:role limit :start,10",nativeQuery = true)
    public List<Member> findByRoleAll(@Param("role") String role,@Param("start")int start);

    @Query(value = "select m.* from member m where m.member_ssn = :memberSsn limit 1",nativeQuery = true)
    public Member findBymemberSsn(@Param("memberSsn")String memberSsn);

    @Query(value = "select m.* from member m where m.member_phone_num =:memberPn limit 1",nativeQuery = true)
    public Member findBymemberPn(@Param("memberPn")String memberPn);

    @Query("select m from member m where m.memberId =:memberId ")
    public Member findBymemberId(@Param("memberId")String memberId);

    @Modifying
    @Transactional
    @Query("update member m set m.memberPw = :memberPw where m.memberId=:memberId")
    public int memberPwUpdate(@Param("memberId")String memberId, @Param("memberPw")String memberPw);

    @Modifying
    @Transactional
    @Query("update member m set m.nickname = :nickname where m.memberId=:memberId")
    public int memberNnUpdate(@Param("memberId")String memberId,@Param("nickname") String nickname);

    @Modifying
    @Transactional
    @Query("update member m set m.memberName =:memberName where m.memberId =:memberId")
    public int memberNameUpdate(@Param("memberId")String memberId,@Param("memberName") String memberName);

    @Modifying
    @Transactional
    @Query("update member m set m.memberPhoneNum =:memberPhoneNum where m.memberId =:memberId")
    public int memberPnUpdate(@Param("memberId")String memberId, @Param("memberPhoneNum") String memberPhoneNum);


}
