package com.rockstars.musiclibrary.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SongDTO {

    private Long id;
    private String name;
    private Long year;
    private String artist;
    private String shortname;
    private Long bpm;
    private Long duration;
    private String genre;
    private String spotifyId;
    private String album;

    public SongDTO() {

    }

}