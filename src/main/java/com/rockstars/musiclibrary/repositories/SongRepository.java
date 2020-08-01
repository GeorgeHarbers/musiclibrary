package com.rockstars.musiclibrary.repositories;

import com.rockstars.musiclibrary.domain.Song;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Repository
@SuppressWarnings("unchecked")
public class SongRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public List<Song> findAll() {
        return entityManager.createNativeQuery("SELECT * FROM songs", Song.class).getResultList();
    }

    public List<Song> findSongsByGenre(String name) {
        return entityManager.createNativeQuery("SELECT * FROM songs WHERE genre = lower(?)", Song.class)
                .setParameter(1, name)
                .getResultList();
    }

    public Song findSongById(Long id) {
        return (Song) entityManager.createNativeQuery("SELECT * FROM songs WHERE id = ?", Song.class)
                .setParameter(1, id)
                .getResultList()
                .stream().findFirst().orElse(null);
    }

    @Transactional
    public Song save(Song song) {
        entityManager.createNativeQuery("INSERT INTO songs (name, year, artist_id, shortname, bpm, duration, genre, spotify_id, album) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", Song.class)
                .setParameter(1, song.getName())
                .setParameter(2, song.getYear())
                .setParameter(3, song.getArtist())
                .setParameter(4, song.getShortname())
                .setParameter(5, song.getBpm())
                .setParameter(6, song.getDuration())
                .setParameter(7, song.getGenre())
                .setParameter(8, song.getSpotifyId())
                .setParameter(9, song.getAlbum())
                .executeUpdate();

        return this.findSongById(this.getLastInsertedId());
    }

    @Transactional
    public Song updateSong(Song song) {
        entityManager.createNativeQuery("UPDATE songs SET name = ?, year = ?, artist_id = ?, shortname = ?, bpm = ?, duration = ? , genre = ?, spotify_id = ?, album = ? WHERE id = ?", Song.class)
                .setParameter(1, song.getName())
                .setParameter(2, song.getYear())
                .setParameter(3, song.getArtist())
                .setParameter(4, song.getShortname())
                .setParameter(5, song.getBpm())
                .setParameter(6, song.getDuration())
                .setParameter(7, song.getGenre())
                .setParameter(8, song.getSpotifyId())
                .setParameter(9, song.getAlbum())
                .setParameter(10, song.getId())
                .executeUpdate();

        return song;
    }

    @Transactional
    public void delete(Long id) {
        entityManager.createNativeQuery("DELETE FROM songs WHERE id = ?", Song.class)
                .setParameter(1, id)
                .executeUpdate();
    }

    public Long getSongCount() {
        return ((BigInteger) entityManager.createNativeQuery("SELECT COUNT(*) FROM songs").getSingleResult()).longValue();
    }

    public Long getLastInsertedId() {
        return ((BigInteger) entityManager.createNativeQuery("SELECT MAX(id) from songs").getSingleResult()).longValue();
    }

    public Song findSongByNameAndShortname(String name, String shortname) {
        return (Song) entityManager.createNativeQuery("SELECT * FROM songs WHERE name = LOWER(?) AND shortname = LOWER(?)", Song.class)
                .setParameter(1, name)
                .setParameter(2, shortname)
                .getResultList()
                .stream().findFirst().orElse(null);
    }
}
