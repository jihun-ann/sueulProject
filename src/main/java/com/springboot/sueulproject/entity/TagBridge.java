package com.springboot.sueulproject.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "tag_bridge")
public class TagBridge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tbid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detailId")
    private Detail detail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tasteTagId")
    private TasteTag tasteTag;
}
