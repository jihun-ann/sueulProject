package com.springboot.sueulproject.mapper;

import com.springboot.sueulproject.entity.TasteTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DetailMapper {
    List<TasteTag> tagFindeAll();
}
