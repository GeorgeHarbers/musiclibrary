package com.rockstars.musiclibrary.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@Table(name = "songs",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "shortname"}),
                @UniqueConstraint(columnNames = {"shortname"})
        })
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long year;

    @NonNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @NonNull
    private String shortname;

    private Long bpm;

    private Long duration;

    private String genre;

    private String spotifyId;

    private String album;

    public Song() {

    }
}
