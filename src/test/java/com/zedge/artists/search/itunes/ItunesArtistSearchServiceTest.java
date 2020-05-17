package com.zedge.artists.search.itunes;

import com.zedge.artists.search.dto.FoundAlbum;
import com.zedge.artists.search.dto.FoundArtist;
import com.zedge.artists.search.itunes.dto._data.ItunesDummyData;
import com.zedge.artists.search.itunes.dto.ItunesEntity;
import com.zedge.artists.search.itunes.dto.ItunesResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItunesArtistSearchServiceTest {

    @Mock
    ItunesHttpClient itunesHttpClient;

    @InjectMocks
    ItunesArtistSearchService target;

    @BeforeEach
    void setUp() {
        clearInvocations(itunesHttpClient);
    }

    @Test
    void searchArtistsByName_shouldCallItunesClient() {
        String name = "Singer name";
        int limit = 10;

        when(itunesHttpClient.searchArtistsByName(name, limit))
                .thenReturn(ItunesResponse.builder()
                        .resultCount(0)
                        .results(Lists.emptyList())
                        .build());

        target.searchArtistsByName(name, limit);

        verify(itunesHttpClient, times(1))
                .searchArtistsByName(name, limit);
    }

    @Test
    void searchArtist_shouldReturnOnlyFoundArtists_whenOnlyArtistsFetched() {
        String name = "Singer name";
        int limit = 10;

        List<ItunesEntity> itunesEntities = List.of(
                ItunesDummyData.itunesArtistBase().build(),
                ItunesDummyData.itunesArtistBase().build(),
                ItunesDummyData.itunesArtistBase().build()
        );

        when(itunesHttpClient.searchArtistsByName(name, limit))
                .thenReturn(ItunesResponse.builder()
                    .resultCount(itunesEntities.size())
                    .results(itunesEntities)
                    .build());

        List<FoundArtist> artists = target.searchArtistsByName(name, limit);
        assertEquals(itunesEntities.size(), artists.size());
    }

    @Test
    void searchArtist_shouldFilterArtists_whenMoreTypesFetched() {
        String name = "Singer name";
        int limit = 10;

        List<ItunesEntity> itunesEntities = List.of(
                ItunesDummyData.itunesAlbumBase().build(),

                ItunesDummyData.itunesArtistBase().build(),
                ItunesDummyData.itunesArtistBase().build(),

                ItunesDummyData.itunesAlbumBase().build(),
                ItunesDummyData.itunesAlbumBase().build()
        );

        when(itunesHttpClient.searchArtistsByName(name, limit))
                .thenReturn(ItunesResponse.builder()
                        .resultCount(itunesEntities.size())
                        .results(itunesEntities)
                        .build());

        List<FoundArtist> artists = target.searchArtistsByName(name, limit);

        assertEquals(2, artists.size());
    }

    @Test
    void topArtistAlbums_shouldCallItunesClient() {
        int artistItunesId = 1010101;
        int limit = 5;

        when(itunesHttpClient.topAlbumsById(artistItunesId, limit))
                .thenReturn(ItunesResponse.builder()
                        .resultCount(1)
                        .results(List.of(ItunesDummyData.itunesAlbumBase().build()))
                        .build());

        target.topArtistAlbums(artistItunesId, limit);

        verify(itunesHttpClient, times(1))
                .topAlbumsById(artistItunesId, limit);
    }

    @Test
    void topAlbumsByAmgId_shouldReturnOrderedAlbums_whenArtistAndAlbumsFetched() {
        int itunesArtistId = 1010101;
        int limit = 5;

        List<ItunesEntity> itunesEntities = List.of(
                ItunesDummyData.itunesArtistBase().build(),
                ItunesDummyData.itunesAlbumBase().collectionName("Album #1").build(),
                ItunesDummyData.itunesAlbumBase().collectionName("Album #2").build(),
                ItunesDummyData.itunesAlbumBase().collectionName("Album #3").build(),
                ItunesDummyData.itunesAlbumBase().collectionName("Album #4").build()
        );

        when(itunesHttpClient.topAlbumsById(itunesArtistId, limit))
                .thenReturn(ItunesResponse.builder()
                        .resultCount(itunesEntities.size())
                        .results(itunesEntities).build());

        List<FoundAlbum> albums = target.topArtistAlbums(itunesArtistId, limit);

        assertEquals(4, albums.size());

        int index = 1;
        for (FoundAlbum album: albums) {
            assertEquals("Album #" + index++, album.getTitle());
        }
    }

    @Test
    void fetchArtist_shouldCallItunesClient() {
        int itunesArtistId = 1010101;

        when(itunesHttpClient.fetchArtist(itunesArtistId))
                .thenReturn(ItunesResponse.builder()
                        .resultCount(1)
                        .results(List.of(ItunesDummyData.itunesArtistBase().build()))
                        .build());

        target.fetchArtist(itunesArtistId);

        verify(itunesHttpClient, times(1))
                .fetchArtist(itunesArtistId);
    }

    @Test
    void fetchArtist_shouldReturnEmpty_whenNoResultsFetched() {
        int itunesArtistId = 1010101;

        when(itunesHttpClient.fetchArtist(itunesArtistId))
                .thenReturn(ItunesResponse.builder()
                        .resultCount(0)
                        .results(Lists.emptyList())
                        .build());

        Optional<?> result = target.fetchArtist(itunesArtistId);

        assertTrue(result.isEmpty());
    }

    @Test
    void fetchArtist_shouldReturnEmpty_whenAlbumReturned() {
        int itunesArtistId = 1010101;

        when(itunesHttpClient.fetchArtist(itunesArtistId))
                .thenReturn(ItunesResponse.builder()
                        .resultCount(1)
                        .results(List.of(ItunesDummyData.itunesAlbumBase().build()))
                        .build());

        Optional<?> result = target.fetchArtist(itunesArtistId);

        assertTrue(result.isEmpty());
    }

    @Test
    void fetchArtist_shouldReturnArtist_whenArtistFetched() {
        int itunesArtistId = 1010101;

        when(itunesHttpClient.fetchArtist(itunesArtistId))
                .thenReturn(ItunesResponse.builder()
                        .resultCount(1)
                        .results(List.of(ItunesDummyData.itunesArtistBase().build()))
                        .build());

        Optional<?> result = target.fetchArtist(itunesArtistId);

        assertTrue(result.isPresent());
    }

    @Test
    void fetchArtist_shouldReturnFirstArtist_whenMoreArtistsFetched() {
        int itunesArtistId = 1010101;

        when(itunesHttpClient.fetchArtist(itunesArtistId))
                .thenReturn(ItunesResponse.builder()
                        .resultCount(2)
                        .results(List.of(
                                ItunesDummyData.itunesArtistBase().artistId(1).build(),
                                ItunesDummyData.itunesArtistBase().artistId(2).build()
                        ))
                        .build());

        Optional<FoundArtist> result = target.fetchArtist(itunesArtistId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getPlatformId());
    }
}