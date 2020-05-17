package com.zedge.artists.service;

import com.zedge.artists.model.Album;
import com.zedge.artists.model.Artist;
import com.zedge.artists.search.ArtistSearchService;
import com.zedge.artists.search.mapper.FoundAlbumMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TopAlbumsService {
    private ArtistSearchService artistSearchService;
    private ArtistsService artistsService;

    private Duration refreshAfter;
    private int topAlbumsLimit;

    public TopAlbumsService(
            ArtistSearchService artistSearchService,
            ArtistsService artistsService,
            @Value("${app.topalbums.refresh.every:7d}") Duration refreshAfter,
            @Value("${app.topalbums.limit:5}") int topAlbumsLimit
    ) {
        this.artistSearchService = artistSearchService;
        this.artistsService = artistsService;
        this.refreshAfter = refreshAfter;
        this.topAlbumsLimit = topAlbumsLimit;
    }

    public Artist ensureArtistTopAlbums(Artist artist) {
        if (! shouldRefreshAlbums(artist)) {
            return artist;
        }

        List<Album> topAlbums = this.artistSearchService
                .topArtistAlbums(artist.getPlatformId(), topAlbumsLimit)
                .stream()
                .map(FoundAlbumMapper::toAlbum)
                .peek(album -> album.setId(UUID.randomUUID()))
                .peek(album -> album.setArtist(artist))
                .collect(Collectors.toList());

        artist.setTopAlbums(topAlbums);
        artist.setTopAlbumsFetchedAt(Instant.now());

        return this.artistsService.save(artist);
    }

    private boolean shouldRefreshAlbums(Artist artist) {
        if (artist.getTopAlbumsFetchedAt() == null) {
            return true;
        }

        Instant refreshTime = Instant.now()
                .minus(refreshAfter.toSeconds(), ChronoUnit.SECONDS);

        return artist.getTopAlbumsFetchedAt().isBefore(refreshTime);
    }
}
