package com.rockstars.musiclibrary.services;

import com.rockstars.musiclibrary.DTO.SongDTO;
import com.rockstars.musiclibrary.domain.Song;

import java.util.List;

public interface SongService {

    List<Song> listSongsByGenre(String genre);

    Song save(SongDTO songDTO);

    Long getSongCount();

    boolean delete(Long id);

    Song updateSong(Long songId, SongDTO songDTO);

}
