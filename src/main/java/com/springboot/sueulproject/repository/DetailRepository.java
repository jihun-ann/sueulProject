package com.springboot.sueulproject.repository;

import com.springboot.sueulproject.entity.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailRepository extends JpaRepository<Detail,Long> {

    @Query("select d from Detail d where d.type.tid = :type order by d.did")
    public List<Detail> findByType(int type);
    @Query("select d from Detail d where d.origin.oid = :origin order by d.did")
    public List<Detail> findByOrigin(int origin);
    @Query("select d from Detail d left join TagBridge tb on d.did = tb.detail.did where tb.tasteTag.tid = :tid order by d.did")
    public List<Detail> findByTasteTag(Long tid);

    @Query("select d from Detail d where d.type.tid =:tid1 or d.type.tid= :tid2 order by d.bookmarkCount desc, d.starRating/d.starRateCount desc, d.did")
    public List<Detail> todayWeather(int tid1,int tid2);
}
