package com.springboot.sueulproject.repository;

import com.springboot.sueulproject.entity.Origin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OriginRepository extends JpaRepository<Origin,Integer> {
}
