package com.rockstars.musiclibrary.mappers;

import com.rockstars.musiclibrary.DTO.SongDTO;
import com.rockstars.musiclibrary.domain.Song;
import org.modelmapper.ModelMapper;

public class SongMapper {

    private final ModelMapper modelMapper;

    public SongMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Maps song object to SongDTO and map the artist to a string
     * @param song source
     * @return songDTO destination
     */
    public SongDTO convertToDTO(Song song) {
        modelMapper.typeMap(Song.class, SongDTO.class).addMappings(mapper ->
            mapper.map(songObject -> songObject.getArtist().getName(), SongDTO::setArtist)
        );
        return modelMapper.map(song, SongDTO.class);
    }

    public Song convertToDomain(SongDTO songDTO) {
        return modelMapper.map(songDTO, Song.class);
    }

}
