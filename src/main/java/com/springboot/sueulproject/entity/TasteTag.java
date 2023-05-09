package com.springboot.sueulproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "taste_tag")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TasteTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tid;
    private String content;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tasteTag",  cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<TagBridge> tbList = new ArrayList<>();
}
