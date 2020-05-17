package com.zedge.artists.service;

import com.zedge.artists.error.ResourceNotFound;
import com.zedge.artists.model.Artist;
import com.zedge.artists.model._data.DummyData;
import com.zedge.artists.persistence.UserPreferenceRepository;
import com.zedge.artists.search.ArtistSearchService;
import com.zedge.artists.search.dto._data.SearchDummyData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPreferencesServiceTest {

    @Mock
    ArtistsService artistsService;

    @Mock
    ArtistSearchService artistSearchService;

    @Mock
    UserPreferenceRepository userPreferenceRepository;

    @Mock
    TopAlbumsService topAlbumsService;

    @InjectMocks
    UserPreferencesService target;

    @BeforeEach
    void setUp() {
        clearInvocations(artistsService, artistSearchService, userPreferenceRepository, topAlbumsService);
    }

    @Test
    void findUserFavoriteArtist_shouldCallRepositoryWithGivenId() {
        UUID id = UUID.randomUUID();

        target.findUserFavoriteArtist(id);

        verify(userPreferenceRepository, times(1))
                .findUserFavoriteArtist(id);
    }

    @Test
    void findUserFavoriteArtist_shouldEnsureTopAlbums_whenArtistIsPresent() {
        UUID id = UUID.randomUUID();
        Artist artist = DummyData.baseArtist().build();

        when(userPreferenceRepository.findUserFavoriteArtist(id))
                .thenReturn(of(artist));

        target.findUserFavoriteArtist(id);

        verify(topAlbumsService, times(1))
                .ensureArtistTopAlbums(artist);
    }

    @Test
    void findUserFavoriteArtist_shouldNotEnsureTopAlbums_whenArtistIsAbsent() {
        UUID id = UUID.randomUUID();

        when(userPreferenceRepository.findUserFavoriteArtist(id))
                .thenReturn(empty());

        target.findUserFavoriteArtist(id);

        verifyNoInteractions(topAlbumsService);
    }

    @Test
    void setUserFavoriteArtist_shouldSearchOldFavorite() {
        UUID userId = UUID.randomUUID();
        int artistPlatformId = 123456;
        Artist oldFavoriteArtist = DummyData.baseArtist()
                .platformId(artistPlatformId)
                .build();

        when(userPreferenceRepository.findUserFavoriteArtist(userId))
                .thenReturn(of(oldFavoriteArtist));

        target.setUserFavoriteArtist(userId, artistPlatformId);

        verify(userPreferenceRepository, times(1))
            .findUserFavoriteArtist(userId);
    }

    @Test
    void setUserFavoriteArtist_shouldDoNothing_whenSameFavoriteSet() {
        UUID userId = UUID.randomUUID();
        int artistPlatformId = 123456;
        Artist oldFavoriteArtist = DummyData.baseArtist()
                .platformId(artistPlatformId)
                .build();

        when(userPreferenceRepository.findUserFavoriteArtist(userId))
                .thenReturn(of(oldFavoriteArtist));

        target.setUserFavoriteArtist(userId, artistPlatformId);

        verifyNoInteractions(artistsService);
    }

    @Test
    void setUserFavoriteArtist_shouldSearchByPlatformId_whenNewUser() {
        UUID userId = UUID.randomUUID();
        int favoritePlatformId = 123456;

        when(userPreferenceRepository.findUserFavoriteArtist(userId))
                .thenReturn(empty());

        when(artistsService.findByPlatformId(favoritePlatformId))
                .thenReturn(of(DummyData.baseArtist().build()));

        target.setUserFavoriteArtist(userId, favoritePlatformId);

        verify(artistsService, times(1))
            .findByPlatformId(favoritePlatformId);
    }

    @Test
    void setUserFavoriteArtist_shouldSavePreferenceAndReturn_whenNewUserAndArtistAlreadySaved() {
        UUID userId = UUID.randomUUID();
        int favoritePlatformId = 123456;

        Artist savedArtist = DummyData.baseArtist()
                .id(UUID.randomUUID())
                .build();

        when(userPreferenceRepository.findUserFavoriteArtist(userId))
                .thenReturn(empty());

        when(artistsService.findByPlatformId(favoritePlatformId))
                .thenReturn(of(savedArtist));

        target.setUserFavoriteArtist(userId, favoritePlatformId);

        verify(userPreferenceRepository, times(1))
                .setUserFavoriteArtist(userId, savedArtist.getId());

        verifyNoInteractions(artistSearchService);
    }

    @Test
    void setUserFavoriteArtist_shouldFetchArtist_whenItIsNotArtistAlreadySaved() {
        UUID userId = UUID.randomUUID();
        int artistPlatformId = 123456;

        when(userPreferenceRepository.findUserFavoriteArtist(userId))
                .thenReturn(empty());

        when(artistsService.findByPlatformId(artistPlatformId))
                .thenReturn(empty());

        when(artistSearchService.fetchArtist(artistPlatformId))
                .thenReturn(of(SearchDummyData.baseFoundArtist().build()));

        when(artistsService.create(any()))
                .thenReturn(DummyData.baseArtist().build());

        target.setUserFavoriteArtist(userId, artistPlatformId);

        verify(artistSearchService, times(1))
            .fetchArtist(artistPlatformId);
    }

    @Test
    void setUserFavoriteArtist_shouldThrowException_whenNoArtistFetched() {
        UUID userId = UUID.randomUUID();
        int artistPlatformId = 123456;

        when(userPreferenceRepository.findUserFavoriteArtist(userId))
                .thenReturn(empty());

        when(artistsService.findByPlatformId(artistPlatformId))
                .thenReturn(empty());

        when(artistSearchService.fetchArtist(artistPlatformId))
                .thenReturn(empty());

        assertThrows(ResourceNotFound.class, () ->
                target.setUserFavoriteArtist(userId, artistPlatformId));
    }

    @Test
    void setUserFavoriteArtist_shouldCreateArtist_whenFound() {
        UUID userId = UUID.randomUUID();
        int artistPlatformId = 123456;

        when(userPreferenceRepository.findUserFavoriteArtist(userId))
                .thenReturn(empty());

        when(artistsService.findByPlatformId(artistPlatformId))
                .thenReturn(empty());

        when(artistSearchService.fetchArtist(artistPlatformId))
                .thenReturn(of(SearchDummyData.baseFoundArtist().build()));

        when(artistsService.create(any()))
                .thenReturn(DummyData.baseArtist().build());

        target.setUserFavoriteArtist(userId, artistPlatformId);

        verify(artistsService, times(1))
                .create(any());
    }

    @Test
    void setUserFavoriteArtist_shouldSavePreferences_whenOldUserAndNewArtistCreated() {
        UUID userId = UUID.randomUUID();
        int artistPlatformId = 123456;

        Artist oldFavorite = DummyData.baseArtist()
                .platformId(9999)
                .build();

        Artist newFavorite = DummyData.baseArtist()
                .id(UUID.randomUUID())
                .build();

        when(userPreferenceRepository.findUserFavoriteArtist(userId))
                .thenReturn(of(oldFavorite));

        when(artistsService.findByPlatformId(artistPlatformId))
                .thenReturn(empty());

        when(artistSearchService.fetchArtist(artistPlatformId))
                .thenReturn(of(SearchDummyData.baseFoundArtist().build()));

        when(artistsService.create(any()))
                .thenReturn(newFavorite);

        target.setUserFavoriteArtist(userId, artistPlatformId);

        verify(userPreferenceRepository, times(1))
                .updateUserFavoriteArtist(userId, newFavorite.getId());
    }

    @Test
    void setUserFavoriteArtist_shouldEnsureTopAlbums_whenNewArtistCreated() {
        UUID userId = UUID.randomUUID();
        int artistPlatformId = 123456;

        Artist newFavorite = DummyData.baseArtist()
                .id(UUID.randomUUID())
                .build();

        when(userPreferenceRepository.findUserFavoriteArtist(userId))
                .thenReturn(empty());

        when(artistsService.findByPlatformId(artistPlatformId))
                .thenReturn(empty());

        when(artistSearchService.fetchArtist(artistPlatformId))
                .thenReturn(of(SearchDummyData.baseFoundArtist().build()));

        when(artistsService.create(any()))
                .thenReturn(newFavorite);

        target.setUserFavoriteArtist(userId, artistPlatformId);

        verify(topAlbumsService, times(1))
                .ensureArtistTopAlbums(newFavorite);
    }
}