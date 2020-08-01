package com.rockstars.musiclibrary.controllers;

import com.rockstars.musiclibrary.DTO.ArtistDTO;
import com.rockstars.musiclibrary.domain.Artist;
import com.rockstars.musiclibrary.services.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/rest/artists/")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Save a new artist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Artist saved successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ArtistDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Artist with name already exists", content = @Content)
    })
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ArtistDTO> newArtist(@RequestBody @NotNull ArtistDTO artistDTO) {
        Artist artist = artistService.save(artistDTO);
        if (artist != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(artist, ArtistDTO.class));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Finds artist by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist with name found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ArtistDTO.class))}),
            @ApiResponse(responseCode = "404", description = "No artist found for name", content = @Content)
    })
    @GetMapping(value = "/{name}", produces = "application/json")
    public ResponseEntity<ArtistDTO> artistByName(@PathVariable @NotNull String name) {
        Artist artist = artistService.findArtistByName(name);

        if (artist != null) {
            return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(artist, ArtistDTO.class));
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @Operation(summary = "Update existing artist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist updated successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ArtistDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Could not update artist", content = @Content)
    })
    @PutMapping(value = "/{artistId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ArtistDTO> updateArtist(@PathVariable @NotNull Long artistId, @RequestBody @NotNull ArtistDTO artistDTO) {
        Artist artist = artistService.updateArtist(artistId, artistDTO);
        if (artist != null) {
            return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(artist, ArtistDTO.class));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Delete existing artist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Artist deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Artist could not be deleted", content = @Content)
    })
    @DeleteMapping(value = "/{artistId}")
    public ResponseEntity<Object> deleteArtist(@PathVariable @NotNull Long artistId) {
        if (artistService.delete(artistId)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
