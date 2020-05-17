package com.zedge.artists.search.mapper;

import com.zedge.artists.model.Artist;
import com.zedge.artists.search.dto.FoundArtist;
import com.zedge.artists.search.dto._data.SearchDummyData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FoundArtistMapperTest {

    @Test
    void toArtist_shouldMapAllRequiredValues() {
        FoundArtist artist = SearchDummyData.baseFoundArtist().build();

        Artist result = FoundArtistMapper.toArtist(artist);

        assertEquals(artist.getPlatformId(), result.getPlatformId());
        assertEquals(artist.getName(), result.getName());
        assertEquals(artist.getAmgId(), result.getAmgId());
        assertEquals(artist.getPrimaryGenreName(), result.getPrimaryGenreName());
    }

    @Test
    void toArtist_shouldAuxiliaryValuesBlank() {
        FoundArtist artist = SearchDummyData.baseFoundArtist().build();

        Artist result = FoundArtistMapper.toArtist(artist);

        assertNull(result.getId());
        assertNull(result.getTopAlbums());
        assertNull(result.getTopAlbumsFetchedAt());
    }
}