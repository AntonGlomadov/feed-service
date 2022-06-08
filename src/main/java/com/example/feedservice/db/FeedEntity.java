package com.example.feedservice.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "feed")
public class FeedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String username;

    private String text;

    private LocalDateTime date;


    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<ContentEntity> contents;


}
