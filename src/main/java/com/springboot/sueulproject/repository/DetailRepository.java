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
}
