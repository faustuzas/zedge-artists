package com.zedge.artists.search.mapper;

import com.zedge.artists.model.Album;
import com.zedge.artists.search.dto.FoundAlbum;
import com.zedge.artists.search.dto._data.SearchDummyData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoundAlbumMapperTest {

    @Test
    void toAlbum_shouldMapAllRequiredValues() {
        FoundAlbum album = SearchDummyData.baseFoundAlbum().build();

        Album result = FoundAlbumMapper.toAlbum(album);

        assertEquals(album.getPlatformId(), result.getPlatformId());
        assertEquals(album.getTitle(), result.getTitle());
        assertEquals(album.getReleaseDate(), result.getReleaseDate());
        assertEquals(album.getTrackCount(), result.getTrackCount());
    }

    @Test
    void toAlbum_shouldAuxiliaryValuesBlank() {
        FoundAlbum album = SearchDummyData.baseFoundAlbum().build();

        Album result = FoundAlbumMapper.toAlbum(album);

        assertNull(result.getId());
        assertNull(result.getArtist());
    }
}