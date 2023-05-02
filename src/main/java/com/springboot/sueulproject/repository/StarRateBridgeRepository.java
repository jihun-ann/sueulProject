package com.springboot.sueulproject.repository;

import com.springboot.sueulproject.entity.StarRateBridge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StarRateBridgeRepository extends JpaRepository<StarRateBridge,Long> {

    @Query("select st from StarRateBridge st where st.member.memberId = :Mid and st.detail.did = :Did")
    public StarRateBridge findByMidDid(String Mid, Long Did);
}