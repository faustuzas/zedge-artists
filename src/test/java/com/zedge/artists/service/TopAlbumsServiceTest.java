package com.zedge.artists.service;

import com.zedge.artists.model.Album;
import com.zedge.artists.model.Artist;
import com.zedge.artists.model._data.DummyData;
import com.zedge.artists.search.ArtistSearchService;
import com.zedge.artists.search.dto.FoundAlbum;
import com.zedge.artists.search.dto._data.SearchDummyData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopAlbumsServiceTest {

    @Captor
    ArgumentCaptor<List<Album>> albumsCaptor;

    @Mock
    ArtistSearchService artistSearchService;

    @Mock
    ArtistsService artistsService;

    @BeforeEach
    void setUp() {
        clearInvocations(artistSearchService, artistsService);
    }

    @Test
    void ensureArtistTopAlbums_shouldNotRefresh_whenTopAlbumsFetchedAtIsNotDue() {
        Instant lastRefreshedAt = Instant.now().minus(5, ChronoUnit.DAYS);
        Duration refreshEvery = Duration.ofDays(7);

        Artist artist = DummyData.baseArtist()
                .topAlbumsFetchedAt(lastRefreshedAt)
                .build();

        TopAlbumsService target = createTargetWithMocks(refreshEvery, 0);

        target.ensureArtistTopAlbums(artist);

        verifyNoInteractions(artistSearchService);
    }

    @Test
    void ensureArtistTopAlbums_shouldRefresh_whenTopAlbumsFetchedAtIsNull() {
        int limit = 5;

        Artist artist = DummyData.baseArtist()
                .topAlbumsFetchedAt(null)
                .build();

        TopAlbumsService target = createTargetWithMocks(null, limit);

        target.ensureArtistTopAlbums(artist);

        verify(artistSearchService, times(1))
            .topArtistAlbums(artist.getPlatformId(), limit);
    }

    @Test
    void ensureArtistTopAlbums_shouldRefresh_whenTopAlbumsFetchedAtIsDue() {
        Instant lastRefreshedAt = Instant.now().minus(10, ChronoUnit.DAYS);
        Duration refreshEvery = Duration.ofDays(7);
        int limit = 5;

        Artist artist = DummyData.baseArtist()
                .topAlbumsFetchedAt(lastRefreshedAt)
                .build();

        TopAlbumsService target = createTargetWithMocks(refreshEvery, limit);

        target.ensureArtistTopAlbums(artist);

        verify(artistSearchService, times(1))
                .topArtistAlbums(artist.getPlatformId(), limit);
    }

    @Test
    void ensureArtistTopAlbums_shouldSetAlbumsForArtist() {
        Instant lastRefreshedAt = Instant.now().minus(10, ChronoUnit.DAYS);
        Duration refreshEvery = Duration.ofDays(7);
        int limit = 5;

        Artist artist = Mockito.spy(DummyData.baseArtist()
                .topAlbums(null)
                .topAlbumsFetchedAt(lastRefreshedAt)
                .build());

        List<FoundAlbum> foundAlbums = SearchDummyData.fullFoundAlbums(limit);

        when(artistSearchService.topArtistAlbums(artist.getPlatformId(), limit))
                .thenReturn(foundAlbums);

        TopAlbumsService target = createTargetWithMocks(refreshEvery, limit);
        target.ensureArtistTopAlbums(artist);

        verify(artist, times(1))
                .setTopAlbums(albumsCaptor.capture());

        List<Album> capturedAlbums = albumsCaptor.getValue();
        assertNotNull(capturedAlbums);
        assertEquals(foundAlbums.size(), capturedAlbums.size());
    }

    @Test
    void ensureArtistTopAlbums_shouldUpdateAlbumsFetchedAt() {
        Artist artist = Mockito.spy(DummyData.baseArtist()
                .topAlbumsFetchedAt(null)
                .build());

        TopAlbumsService target = createTargetWithMocks(null, 0);
        target.ensureArtistTopAlbums(artist);

        ArgumentCaptor<Instant> instantCaptor = ArgumentCaptor.forClass(Instant.class);
        verify(artist, times(1))
                .setTopAlbumsFetchedAt(instantCaptor.capture());

        Instant capturedInstant = instantCaptor.getValue();

        // Give 60 seconds error for slow test
        assertTrue(capturedInstant.isAfter(Instant.now().minus(60, ChronoUnit.SECONDS)));
    }

    @Test
    void ensureArtistTopAlbums_shouldSetAttributesForNewAlbums() {
        Instant lastRefreshedAt = Instant.now().minus(10, ChronoUnit.DAYS);
        Duration refreshEvery = Duration.ofDays(7);
        int limit = 5;

        Artist artist = Mockito.spy(DummyData.baseArtist()
                .topAlbumsFetchedAt(lastRefreshedAt)
                .build());

        List<FoundAlbum> foundAlbums = SearchDummyData.fullFoundAlbums(limit);

        when(artistSearchService.topArtistAlbums(artist.getPlatformId(), limit))
                .thenReturn(foundAlbums);

        TopAlbumsService target = createTargetWithMocks(refreshEvery, limit);
        target.ensureArtistTopAlbums(artist);

        verify(artist, times(1))
                .setTopAlbums(albumsCaptor.capture());

        List<Album> capturedAlbums = albumsCaptor.getValue();
        assertEquals(limit, capturedAlbums.size());

        capturedAlbums.forEach(album -> assertNotNull(album.getId()));
        capturedAlbums.forEach(album -> assertEquals(artist, album.getArtist()));
    }

    @Test
    void ensureArtistTopAlbums_shouldSaveArtist() {
        Artist artist = Mockito.spy(DummyData.baseArtist()
                .topAlbumsFetchedAt(null)
                .build());

        TopAlbumsService target = createTargetWithMocks(null, 0);
        target.ensureArtistTopAlbums(artist);

        verify(artistsService, times(1))
                .save(artist);
    }

    private TopAlbumsService createTargetWithMocks(Duration duration, int limit) {
        return new TopAlbumsService(artistSearchService, artistsService, duration, limit);
    }
}