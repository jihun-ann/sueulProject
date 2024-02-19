package com.springboot.sueulproject.repository;

import com.springboot.sueulproject.entity.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailRepository extends JpaRepository<Detail,Long> {


    @Query("select d from detail d where d.type.tid = :type order by d.did")
    public List<Detail> findByType(@Param("type") int type);
    @Query("select d from detail d where d.origin.oid = :origin order by d.did")
    public List<Detail> findByOrigin(@Param("origin")int origin);
    @Query("select d from detail d left join tag_bridge tb on d.did = tb.detail.did where tb.tasteTag.tid = :tid order by d.did")
    public List<Detail> findByTasteTag(@Param("tid")Long tid);

    @Query("select d from detail d where d.type.tid =:tid1 or d.type.tid = :tid2 order by rand()")
    public List<Detail> todayWeather(@Param("tid1") int tid1,@Param("tid2") int tid2);

    @Query("select d from detail d left join bookmark_bridge bb on d.did = bb.detail.did where bb.member =:memberid")
    public List<Detail> bookmarkFindByMemberId(@Param("memberid") String memberid);
}
