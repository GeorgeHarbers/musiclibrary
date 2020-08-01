package com.rockstars.musiclibrary.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@Table(name = "artists", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name", name = "uniqueNameConstraint")}
)
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Artist() {
    }
}
