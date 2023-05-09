package com.springboot.sueulproject.repository;

import com.springboot.sueulproject.entity.TagBridge;
import com.springboot.sueulproject.entity.TasteTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagBridgeRepository extends JpaRepository<TagBridge,Long> {

    @Query("select tt from tag_bridge tb left join taste_tag tt on tb.tasteTag.tid = tt.tid  where tb.detail.did = :did")
    public List<TasteTag> findByDetailId(Long did);

    @Query("select tb from tag_bridge tb where tb.detail.did =:did")
    public List<TagBridge> findByDetailIdTagBridge(Long did);
}
