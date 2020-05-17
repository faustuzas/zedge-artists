package com.zedge.artists.service;

import com.zedge.artists.model.Artist;
import com.zedge.artists.persistence.ArtistsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ArtistsService {
    private ArtistsRepository artistsRepository;

    public ArtistsService(ArtistsRepository artistsRepository) {
        this.artistsRepository = artistsRepository;
    }

    public Optional<Artist> findByPlatformId(int artistPlatformId) {
        return this.artistsRepository.findByPlatformId(artistPlatformId);
    }

    public Artist create(Artist artist) {
        artist.setId(UUID.randomUUID());
        return save(artist);
    }

    public Artist save(Artist artist) {
        return this.artistsRepository.save(artist);
    }
}
