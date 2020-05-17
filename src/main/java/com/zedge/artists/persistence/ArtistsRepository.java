package com.zedge.artists.persistence;

import com.zedge.artists.model.Artist;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ArtistsRepository extends CrudRepository<Artist, UUID> {

    Optional<Artist> findByPlatformId(int platformId);
}
