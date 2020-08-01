package com.rockstars.musiclibrary.services.impl;

import com.rockstars.musiclibrary.DTO.ArtistDTO;
import com.rockstars.musiclibrary.DTO.SongDTO;
import com.rockstars.musiclibrary.domain.Artist;
import com.rockstars.musiclibrary.domain.Song;
import com.rockstars.musiclibrary.mappers.SongMapper;
import com.rockstars.musiclibrary.repositories.SongRepository;
import com.rockstars.musiclibrary.services.ArtistService;
import com.rockstars.musiclibrary.services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImpl implements SongService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private SongMapper songMapper;

    @Override
    public List<Song> listSongsByGenre(String genre) {
        return this.songRepository.findSongsByGenre(genre);
    }

    @Override
    public Song save(SongDTO songDTO) {
        Song song = this.songRepository.findSongByNameAndShortname(songDTO.getName(), songDTO.getShortname());
        if (song == null) {
            song = songMapper.convertToDomain(songDTO);

            Artist artist = artistService.findArtistByName(songDTO.getArtist());

            if (artist == null) { // Artist does nog exists, create a new one
                ArtistDTO artistDTO = new ArtistDTO();
                artistDTO.setName(songDTO.getArtist());
                artist = artistService.save(artistDTO); // we only accept DTO's in the service
            }

            song.setArtist(artist);

            return songRepository.save(song);
        } else { // song with name and shortname already exists
            return null;
        }
    }

    @Override
    public boolean delete(Long id) {
        Song song = this.songRepository.findSongById(id);
        if (song != null) {
            this.songRepository.delete(song.getId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Long getSongCount() {
        return songRepository.getSongCount();
    }

    @Override
    public Song updateSong(Long songId, SongDTO songDTO) {
        Song songToUpdate = this.songRepository.findSongById(songId);
        if (songToUpdate == null) { // song does exist so we can update it
            return null;
        }

        songToUpdate.setName(songDTO.getName());
        songToUpdate.setYear(songDTO.getYear());
        songToUpdate.setShortname(songDTO.getShortname());
        songToUpdate.setBpm(songDTO.getBpm());
        songToUpdate.setDuration(songDTO.getDuration());
        songToUpdate.setGenre(songDTO.getGenre());
        songToUpdate.setSpotifyId(songDTO.getSpotifyId());
        songToUpdate.setAlbum(songDTO.getAlbum());

        if (songDTO.getArtist() != null && // artist could be null in an update so we have to check it
                !songToUpdate.getArtist().getName().equals(songDTO.getArtist())) { // different artist
            Artist artist = artistService.findArtistByName(songDTO.getArtist());
            if (artist != null) {
                songToUpdate.setArtist(artist);
            } else {
                return null; // new artists should be added via the endpoint
            }
        }

        return songRepository.updateSong(songToUpdate);
    }
}
