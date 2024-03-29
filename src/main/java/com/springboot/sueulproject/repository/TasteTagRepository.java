package com.springboot.sueulproject.repository;

import com.springboot.sueulproject.entity.TasteTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasteTagRepository extends JpaRepository<TasteTag,Long> {

    @Query("select t from taste_tag t where t.content like :tagName%")
    public List<TasteTag> tagFindByName(@Param("tagName") String tagName);

    @Query("select t from taste_tag t where t.tid=:tagId")
    public TasteTag tagFindByIdOne(@Param("tagId") Long tagId);
}
