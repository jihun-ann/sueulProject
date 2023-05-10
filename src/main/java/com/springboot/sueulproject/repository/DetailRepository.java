package com.springboot.sueulproject.repository;

import com.springboot.sueulproject.entity.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailRepository extends JpaRepository<Detail,Long> {

    @Query("select d from detail d where d.type.tid = :type order by d.did")
    public List<Detail> findByType(int type);
    @Query("select d from detail d where d.origin.oid = :origin order by d.did")
    public List<Detail> findByOrigin(int origin);
    @Query("select d from detail d left join tag_bridge tb on d.did = tb.detail.did where tb.tasteTag.tid = :tid order by d.did")
    public List<Detail> findByTasteTag(Long tid);

    @Query(value = "select * from detail where type_id =:tid1 or type_id= :tid2 order by rand()",nativeQuery = true)
    public List<Detail> todayWeather(int tid1,int tid2);

    @Query(value = "select d.* from detail d left join bookmark_bridge bb on d.did = bb.detail_id where bb.member_id =:memberid",nativeQuery = true)
    public List<Detail> bookmarkFindByMemberId(String memberid);
}
