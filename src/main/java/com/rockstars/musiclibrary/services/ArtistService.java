package com.rockstars.musiclibrary.services;

import com.rockstars.musiclibrary.DTO.ArtistDTO;
import com.rockstars.musiclibrary.domain.Artist;

public interface ArtistService {

    Artist findArtistByName(String name);

    Artist save(ArtistDTO artistDTO);

    boolean delete(Long id);

    Artist updateArtist(Long id, ArtistDTO artistDTO);

}
