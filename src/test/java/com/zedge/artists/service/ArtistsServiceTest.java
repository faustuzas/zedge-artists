package com.zedge.artists.service;

import com.zedge.artists.model.Artist;
import com.zedge.artists.model._data.DummyData;
import com.zedge.artists.persistence.ArtistsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistsServiceTest {

    @Mock
    ArtistsRepository artistsRepository;

    @InjectMocks
    ArtistsService target;

    @BeforeEach
    void setUp() {
        clearInvocations(artistsRepository);
    }

    @Test
    void findByPlatformId_shouldCallRepository_withGivenId() {
        int artistPlatformId = 1234;

        target.findByPlatformId(artistPlatformId);

        verify(artistsRepository, times(1))
                .findByPlatformId(artistPlatformId);
    }

    @Test
    void findByPlatformId_shouldReturnRepositoryResult() {
        int artistPlatformId = 1234;

        Optional<Artist> repoResult = Optional.of(DummyData.baseArtist().build());
        when(artistsRepository.findByPlatformId(artistPlatformId))
                .thenReturn(repoResult);

        Optional<Artist> result = target.findByPlatformId(artistPlatformId);

        assertEquals(repoResult, result);
    }

    @Test
    void save_shouldCallRepository() {
        Artist artist = DummyData.baseArtist().build();

        target.save(artist);

        verify(artistsRepository, times(1))
                .save(artist);
    }

    @Test
    void create_shouldCallRepository() {
        Artist artist = DummyData.baseArtist().build();

        target.create(artist);

        verify(artistsRepository, times(1))
                .save(artist);
    }

    @Test
    void create_shouldSetGeneratedIdForArtist() {
        Artist artist = DummyData.baseArtist()
                .id(null)
                .build();

        target.create(artist);

        assertNotNull(artist.getId());
    }
}