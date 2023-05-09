package com.springboot.sueulproject.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "star_rate_bridge")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StarRateBridge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bid;

    @OneToOne
    @JoinColumn(name="detailId")
    private Detail detail;

    @OneToOne
    @JoinColumn(name="memberId")
    private Member member;

    private int starScore;
}
