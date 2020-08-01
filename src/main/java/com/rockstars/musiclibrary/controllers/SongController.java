package com.rockstars.musiclibrary.controllers;

import com.rockstars.musiclibrary.DTO.SongDTO;
import com.rockstars.musiclibrary.domain.Song;
import com.rockstars.musiclibrary.mappers.SongMapper;
import com.rockstars.musiclibrary.services.ArtistService;
import com.rockstars.musiclibrary.services.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/songs/")
public class SongController {

    @Autowired
    private SongService songService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private SongMapper songMapper;

    @Operation(summary = "Save a new song")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Song saved successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SongDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Song with name and shortname already exists", content = @Content)
    })
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<SongDTO> newSong(@RequestBody @NotNull SongDTO songDTO) {
        Song song = songService.save(songDTO);
        if (song != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(songMapper.convertToDTO(song));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Finds songs by genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found songs with genre", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SongDTO.class))}),
            @ApiResponse(responseCode = "404", description = "No songs found for genre", content = @Content)
    })
    @GetMapping(value = "/{genre}", produces = "application/json")
    public ResponseEntity<List<SongDTO>> songsByGenre(@PathVariable @NotNull String genre) {
        List<SongDTO> songsByGenre = songService.listSongsByGenre(genre).stream()
                .map(songMapper::convertToDTO)
                .collect(Collectors.toList());

        if (songsByGenre.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(songsByGenre);
        }
    }

    @Operation(summary = "Update existing song")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Song updated successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SongDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Could not update song", content = @Content)
    })
    @PutMapping(value = "/{songId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<SongDTO> updateSong(@PathVariable @NotNull Long songId, @RequestBody @NotNull SongDTO songDTO) {
        Song song = songService.updateSong(songId, songDTO);
        if (song != null) {
            return ResponseEntity.status(HttpStatus.OK).body(songMapper.convertToDTO(song));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Delete existing song")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Song deleted successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Song could not be deleted", content = @Content)
    })
    @DeleteMapping(value = "/{songId}")
    public ResponseEntity<Object> deleteSong(@PathVariable @NotNull Long songId) {
        if (songService.delete(songId)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
