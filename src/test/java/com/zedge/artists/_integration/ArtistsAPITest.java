package com.zedge.artists._integration;

import com.zedge.artists.search.itunes.ItunesHttpClient;
import com.zedge.artists.search.itunes.dto.ItunesArtist;
import com.zedge.artists.search.itunes.dto.ItunesResponse;
import com.zedge.artists.search.itunes.dto._data.ItunesDummyData;
import com.zedge.artists.web.payload._data.WebPayloadDummyData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = "test-db")
public class ArtistsAPITest extends BaseAPITest {

    @Autowired
    private ItunesHttpClient itunesClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        clearInvocations(itunesClient);
    }

    @Test
    void searchArtists_shouldReturnFoundData() throws Exception {
        String term = "elvis";
        ItunesArtist itunesArtist = ItunesDummyData.itunesArtistBase()
                .artistName("Elvis Presley")
                .build();

        when(itunesClient.searchArtistsByName(anyString(), anyInt()))
                .thenReturn(ItunesResponse.builder()
                        .resultCount(1)
                        .results(List.of(itunesArtist))
                        .build());

        get("/artists/search?term=" + term)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].name", is(itunesArtist.getArtistName())))
                .andExpect(jsonPath("$.[0].platformId", is(itunesArtist.getArtistId())))
                .andExpect(jsonPath("$.[0].amgId", is(itunesArtist.getAmgArtistId())))
                .andExpect(jsonPath("$.[0].primaryGenreName", is(itunesArtist.getPrimaryGenreName())));

        verify(itunesClient, times(1))
                .searchArtistsByName(term, 10);
    }

    @Test
    void searchArtists_shouldReturnError_whenTermNotProvided() throws Exception {
        get("/artists/search")
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFavoriteArtistTopAlbums_shouldReturnError_whenNoFavoriteSet() throws Exception {
        get("/artists/favorite?userId=" + UUID.randomUUID())
                .andExpect(status().isNotFound());
    }

    @Test
    void getFavoriteArtistTopAlbums_shouldReturnAlbums_whenFavoriteSet() throws Exception {
        get("/artists/favorite?userId=" + USER_ID)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$.[2].id", is("a3c11bb2-8226-41df-be45-62035942ca1b")))
                .andExpect(jsonPath("$.[2].platformId", is(1236)))
                .andExpect(jsonPath("$.[2].trackCount", is(12)))
                .andExpect(jsonPath("$.[2].releaseDate", is("2011-01-14")));
    }

    @Test
    void saveFavoriteArtist_shouldFetchAndSaveFavoriteArtistAndAlbums_whenArtistIsNotSaved() throws Exception {
        int artistPlatformId = 12345890;
        UUID userId = UUID.randomUUID();
        int albumsLimit = 5;

        var payload = WebPayloadDummyData.baseSaveFavoriteArtistPayload()
                .artistPlatformId(artistPlatformId)
                .build();

        var artist = ItunesDummyData.itunesArtistBase()
                .artistId(artistPlatformId)
                .build();

        assertTrue(queryUserFavoriteArtistId(userId).isEmpty());
        assertTrue(queryArtistIdByPlatformId(artistPlatformId).isEmpty());

        when(itunesClient.fetchArtist(anyInt()))
                .thenReturn(ItunesResponse.builder()
                        .resultCount(1)
                        .results(List.of(artist))
                        .build());

        when(itunesClient.topAlbumsById(anyInt(), anyInt()))
                .thenReturn(ItunesResponse.builder()
                        .resultCount(albumsLimit)
                        .results(ItunesDummyData.fullAlbums(albumsLimit))
                        .build());

        post("/artists/favorite?userId=" + userId + "&limit=" + albumsLimit, payload)
                .andExpect(status().isNoContent());

        verify(itunesClient, times(1))
                .fetchArtist(artistPlatformId);

        verify(itunesClient, times(1))
                .topAlbumsById(artistPlatformId, albumsLimit);

        Optional<UUID> artistId = queryUserFavoriteArtistId(userId);
        assertTrue(artistId.isPresent());
        assertTrue(queryArtistIdByPlatformId(artistPlatformId).isPresent());
        assertEquals(albumsLimit, countArtistAlbums(artistId.get()));
    }

    @Test
    void saveFavoriteArtist_shouldSaveUserPreference_whenArtistIsSaved() throws Exception {
        UUID newUserId = UUID.fromString("76df7fc2-f291-4dcb-a7ef-118c1445a3e3");
        int artistPlatformId = 12345; // from SQL

        var payload = WebPayloadDummyData.baseSaveFavoriteArtistPayload()
                .artistPlatformId(artistPlatformId)
                .build();

        assertTrue(queryUserFavoriteArtistId(newUserId).isEmpty());
        assertTrue(queryArtistIdByPlatformId(artistPlatformId).isPresent());

        post("/artists/favorite?userId=" + newUserId, payload)
                .andExpect(status().isNoContent());

        verifyNoInteractions(itunesClient);
        assertTrue(queryUserFavoriteArtistId(newUserId).isPresent());
    }

    private Optional<UUID> queryArtistIdByPlatformId(int platformId) {
        return execQueryForObject(
                "SELECT id FROM artists WHERE platform_id = ?",
                new Object[] { platformId },
                UUID.class
        );
    }

    private Integer countArtistAlbums(UUID artistId) {
        return jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM albums WHERE artist_id = ?", new Object[] { artistId },
                    Integer.class);

    }

    private Integer countAllAlbums() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM albums", Integer.class);

    }

    private Optional<UUID> queryUserFavoriteArtistId(UUID userId) {
        return execQueryForObject(
                "SELECT artist_id FROM users_favorite_artists " +
                     "WHERE user_id = ?", new Object[] { userId },
                UUID.class);
    }

    private <T> Optional<T> execQueryForObject(String query, Object[] args, Class<T> clazz) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, args, clazz));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
