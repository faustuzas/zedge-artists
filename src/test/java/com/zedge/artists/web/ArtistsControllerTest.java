package com.zedge.artists.web;

import com.zedge.artists.error.ResourceNotFound;
import com.zedge.artists.model.Artist;
import com.zedge.artists.model._data.DummyData;
import com.zedge.artists.search.ArtistSearchService;
import com.zedge.artists.search.dto.FoundArtist;
import com.zedge.artists.search.dto._data.SearchDummyData;
import com.zedge.artists.service.UserPreferencesService;
import com.zedge.artists.web.payload.SaveFavoriteArtistPayload;
import com.zedge.artists.web.payload._data.WebPayloadDummyData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistsControllerTest {

    @Mock
    ArtistSearchService artistSearchService;

    @Mock
    UserPreferencesService userPreferencesService;

    @InjectMocks
    ArtistsController target;

    @Test
    void searchArtists_shouldCallSearchService() {
        String term = "name";
        int limit = 6;

        target.searchArtists(term, limit);

        verify(artistSearchService, times(1))
                .searchArtistsByName(term, limit);
    }

    @Test
    void searchArtists_shouldReturnFoundArtists_whenResultReturned() {
        String term = "name";
        int limit = 6;

        List<FoundArtist> foundArtists = SearchDummyData.fullFoundArtists(limit);

        when(artistSearchService.searchArtistsByName(term, limit))
                .thenReturn(foundArtists);

        var result = target.searchArtists(term, limit);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(foundArtists.size(), result.getBody().size());
    }

    @Test
    void getFavoriteArtistTopAlbums_shouldThrowException_whenNoFavoriteFound() {
        UUID userId = UUID.randomUUID();

        assertThrows(ResourceNotFound.class, () ->
                target.getFavoriteArtistTopAlbums(userId));
    }

    @Test
    void getFavoriteArtistTopAlbums_shouldReturnAlbums_whenFavoriteFound() {
        UUID userId = UUID.randomUUID();
        Artist favorite = DummyData.baseArtist()
                .topAlbums(DummyData.fullAlbums(3))
                .build();

        when(userPreferencesService.findUserFavoriteArtist(userId))
                .thenReturn(Optional.of(favorite));

        var result = target.getFavoriteArtistTopAlbums(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(favorite.getTopAlbums().size(), result.getBody().size());
    }

    @Test
    void saveFavoriteArtist_shouldCallPreferenceServiceAndReturnNull() {
        UUID userId = UUID.randomUUID();
        SaveFavoriteArtistPayload payload = WebPayloadDummyData.baseSaveFavoriteArtistPayload()
                .build();

        var result = target.saveFavoriteArtist(userId, payload);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }
}