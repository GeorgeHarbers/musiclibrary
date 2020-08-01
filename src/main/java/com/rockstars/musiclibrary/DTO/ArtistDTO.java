package com.rockstars.musiclibrary.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ArtistDTO {

    private Long id;
    private String name;

    public ArtistDTO() {
    }
}
