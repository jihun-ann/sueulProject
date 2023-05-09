package com.springboot.sueulproject.repository;

import com.springboot.sueulproject.entity.BookmarkBridge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BookmarkBridgeRepository extends JpaRepository<BookmarkBridge,Long> {

    @Query("select bm from bookmark_bridge bm where bm.member.memberId = :Mid and bm.detail.did = :Did")
    public BookmarkBridge findByMidDid(String Mid, Long Did);

    @Modifying
    @Transactional
    @Query("delete from bookmark_bridge bm where bm.detail.did =:did")
    public int deleteByDidAll(Long did);
}
