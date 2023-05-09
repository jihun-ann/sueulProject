package com.springboot.sueulproject.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "bookmark_bridge")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookmarkBridge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bid;

    @OneToOne
    @JoinColumn(name="detailId")
    private Detail detail;

    @OneToOne
    @JoinColumn(name="memberId")
    private Member member;


}
