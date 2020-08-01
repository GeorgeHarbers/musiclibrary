package com.rockstars.musiclibrary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockstars.musiclibrary.DTO.ArtistDTO;
import com.rockstars.musiclibrary.DTO.SongDTO;
import com.rockstars.musiclibrary.services.ArtistService;
import com.rockstars.musiclibrary.services.SongService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Initializer implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Initializer.class);

    @Autowired
    private ArtistService artistService;

    @Autowired
    private SongService songService;

    @Override
    public void run(ApplicationArguments args) {
        if (songService.getSongCount() <= 0) {
            processArtistsAndSongs();
        } else {
            LOGGER.info("Skipping initializer because database contains songs and artists");
        }
    }

    private void processArtistsAndSongs() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        List<SongDTO> songsFromFile = readSongs(objectMapper);
        List<ArtistDTO> artistsFromFile = readArtists(objectMapper);

        storeSongsAndArtists(songsFromFile, artistsFromFile);
    }

    private List<SongDTO> readSongs(ObjectMapper objectMapper) {
        List<SongDTO> songs = new ArrayList<>();
        try {
            InputStream inputStream = TypeReference.class.getResourceAsStream("/songs.json");
            songs = objectMapper.readValue(inputStream, new TypeReference<List<SongDTO>>() {
            });
            // We only want metal bands and songs before 2016
            songs = filterMetalSongsBefore2016(songs);
        } catch (IOException e) {
            LOGGER.error("Error reading songs.json: {}", e.getMessage());
        }
        return songs;
    }

    private List<ArtistDTO> readArtists(ObjectMapper objectMapper) {
        List<ArtistDTO> artists = new ArrayList<>();
        try {
            InputStream inputStream = TypeReference.class.getResourceAsStream("/artists.json");
            artists = objectMapper.readValue(inputStream, new TypeReference<List<ArtistDTO>>() {
            });
        } catch (IOException e) {
            LOGGER.error("Error reading artists.json: {}", e.getMessage());
        }
        return artists;
    }

    private void storeSongsAndArtists(List<SongDTO> songsFromFile, List<ArtistDTO> artistsFromFile) {
        // read all artists from the songs
        List<String> artistsFromSongs = songsFromFile.stream().map(SongDTO::getName).collect(Collectors.toList());
        // we only want so persist artists that are also listed in de artist.json file
        List<ArtistDTO> artistsToPersist = findArtistsInList(artistsFromFile, artistsFromSongs);

        storeSongs(songsFromFile);

        storeArtists(artistsToPersist);

    }

    private List<SongDTO> filterMetalSongsBefore2016(List<SongDTO> SongDTOList) {
        return SongDTOList.stream()
                .filter(song -> song.getGenre().toLowerCase().contains("metal") && song.getYear() < 2016)
                .collect(Collectors.toList());
    }

    private List<ArtistDTO> findArtistsInList(List<ArtistDTO> ArtistDTOList, List<String> artistFromSongs) {
        return ArtistDTOList.stream()
                .filter(artist -> artistFromSongs.contains(artist.getName()))
                .collect(Collectors.toList());
    }

    private void storeSongs(List<SongDTO> songsFromFile) {
        for (SongDTO songDTO : songsFromFile) {
            songDTO.setId(null); // we autogenerate this
            songService.save(songDTO);
        }
    }

    private void storeArtists(List<ArtistDTO> artistsTPersist) {
        for (ArtistDTO artistDTO : artistsTPersist) {
            artistDTO.setId(null); // we autogenerate this
            artistService.save(artistDTO);
        }
    }
}

