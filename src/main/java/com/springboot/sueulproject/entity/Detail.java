package com.springboot.sueulproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long did;
    private String name;
    private String content;
    @ColumnDefault("0")
    private int starRating;
    @ColumnDefault("0")
    private int starRateCounting;
    @ColumnDefault("0")
    private int saveCount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "detail", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    List<TagBridge> tbList = new ArrayList<>();

    @OneToOne
    @JoinColumn(name="originId")
    private Origin origin;

    @OneToOne
    @JoinColumn(name="typeId")
    private Type type;
}
