package com.springboot.sueulproject.repository;

import com.springboot.sueulproject.entity.StarRateBridge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface StarRateBridgeRepository extends JpaRepository<StarRateBridge,Long> {

    @Query("select st from star_rate_bridge st where st.member.memberId = :Mid and st.detail.did = :Did")
    public StarRateBridge findByMidDid(@Param("Mid") String Mid,@Param("Did") Long Did);

    @Modifying
    @Transactional
    @Query("delete from star_rate_bridge st where st.detail.did =:did")
    public int deleteByDidAll(@Param("did") Long did);
}
