package com.springboot.sueulproject.repository;

import com.springboot.sueulproject.entity.TagBridge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagBridgeRepository extends JpaRepository<TagBridge,Long> {
}
