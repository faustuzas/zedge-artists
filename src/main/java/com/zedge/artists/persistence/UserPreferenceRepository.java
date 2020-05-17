package com.zedge.artists.persistence;

import com.zedge.artists.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface UserPreferenceRepository extends JpaRepository<Artist, UUID> {

    @Query(value = "SELECT * FROM artists a " +
                   "JOIN users_favorite_artists ufa ON a.id = ufa.artist_id " +
                   "WHERE ufa.user_id = :userId", nativeQuery = true)
    Optional<Artist> findUserFavoriteArtist(UUID userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_favorite_artists (user_id, artist_id) " +
                   "VALUES (:userId, :artistId)", nativeQuery = true)
    void setUserFavoriteArtist(UUID userId, UUID artistId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users_favorite_artists " +
                   "SET artist_id = :artistId " +
                   "WHERE user_id = :userId", nativeQuery = true)
    void updateUserFavoriteArtist(UUID userId, UUID artistId);
}
