package com.springboot.sueulproject.repository;

import com.springboot.sueulproject.entity.BookmarkBridge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkBridgeRepository extends JpaRepository<BookmarkBridge,Long> {

    @Query("select bm from BookmarkBridge bm where bm.member.memberId = :Mid and bm.detail.did = :Did")
    public BookmarkBridge findByMidDid(String Mid, Long Did);
}
