package com.rockstars.musiclibrary.repositories;

import com.rockstars.musiclibrary.domain.Artist;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Repository
@SuppressWarnings("unchecked")
public class ArtistRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public List<Artist> findAll() {
        return entityManager.createNativeQuery("SELECT * FROM artists", Artist.class).getResultList();
    }

    public Artist findArtistByName(String name) {
        return (Artist) entityManager.createNativeQuery("SELECT * FROM artists WHERE name = lower(?)", Artist.class)
                .setParameter(1, name)
                .getResultList()
                .stream().findFirst().orElse(null);
    }

    public Artist findArtistById(Long id) {
        return (Artist) entityManager.createNativeQuery("SELECT * FROM artists WHERE id = ?", Artist.class)
                .setParameter(1, id)
                .getResultList()
                .stream().findFirst().orElse(null);
    }

    @Transactional
    public Artist save(Artist artist) {
        entityManager.createNativeQuery("INSERT INTO artists (id, name) VALUES (?, ?)", Artist.class)
                .setParameter(1, artist.getId())
                .setParameter(2, artist.getName())
                .executeUpdate();

        return this.findArtistById(getLastInsertedId());
    }

    @Transactional
    public Artist updateArtist(Artist artist) {
        entityManager.createNativeQuery("UPDATE artists SET name = ? WHERE id = ?", Artist.class)
                .setParameter(1, artist.getName())
                .setParameter(2, artist.getId())
                .executeUpdate();

        return artist;
    }

    @Transactional
    public void delete(Long id) {
        entityManager.createNativeQuery("DELETE FROM artists WHERE id = ?", Artist.class)
                .setParameter(1, id)
                .executeUpdate();
    }

    public Long getLastInsertedId() {
        return ((BigInteger) entityManager.createNativeQuery("SELECT MAX(id) from artists").getSingleResult()).longValue();
    }

}
