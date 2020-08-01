package com.rockstars.musiclibrary.services.impl;

import com.rockstars.musiclibrary.DTO.ArtistDTO;
import com.rockstars.musiclibrary.domain.Artist;
import com.rockstars.musiclibrary.repositories.ArtistRepository;
import com.rockstars.musiclibrary.services.ArtistService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArtistServiceImpl implements ArtistService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ArtistRepository artistRepository;

    @Override
    public Artist findArtistByName(String name) {
        return artistRepository.findArtistByName(name);
    }

    @Override
    public Artist save(ArtistDTO artistDTO) {
        Artist artist = this.artistRepository.findArtistByName(artistDTO.getName());
        if (artist == null) {
            artist = modelMapper.map(artistDTO, Artist.class);
            return artistRepository.save(artist);
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(Long artistId) {
        Artist artist = this.artistRepository.findArtistById(artistId);
        if (artist != null) {
            artistRepository.delete(artistId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Artist updateArtist(Long artistId, ArtistDTO artistDTO) {
        Artist artistToUpdate = this.artistRepository.findArtistById(artistId);
        if (artistToUpdate == null) { //artist could not be found
            return null;
        }

        Artist artist = this.artistRepository.findArtistByName(artistDTO.getName());
        if (artist != null && !artistToUpdate.getId().equals(artist.getId())) { // artist with name already exists hence different id's
            return null;
        }

        artistToUpdate.setName(artistDTO.getName());
        return artistRepository.updateArtist(artistToUpdate);
    }
}
