package com.zedge.artists.search.itunes.mapper;

import com.zedge.artists.search.dto.FoundAlbum;
import com.zedge.artists.search.itunes.dto._data.ItunesDummyData;
import com.zedge.artists.search.itunes.dto.ItunesAlbum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItunesAlbumMapperTest {

    @Test
    void toAlbum_shouldMapAllRequiredValues() {
        ItunesAlbum itunesAlbum = ItunesDummyData.itunesAlbumBase()
                .build();

        FoundAlbum album = ItunesAlbumMapper.toFoundAlbum(itunesAlbum);

        assertEquals(itunesAlbum.getCollectionId(), album.getPlatformId());
        assertEquals(itunesAlbum.getCollectionName(), album.getTitle());
        assertEquals(itunesAlbum.getTrackCount(), album.getTrackCount());
        assertEquals(itunesAlbum.getReleaseDate(), album.getReleaseDate());
    }
}