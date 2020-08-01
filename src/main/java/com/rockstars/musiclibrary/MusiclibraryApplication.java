package com.rockstars.musiclibrary;

import com.rockstars.musiclibrary.mappers.SongMapper;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MusiclibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusiclibraryApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public SongMapper songMapper() { return new SongMapper(modelMapper());
    }
}
