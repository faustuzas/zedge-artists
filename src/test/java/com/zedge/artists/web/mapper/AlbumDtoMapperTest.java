package com.zedge.artists.web.mapper;

import com.zedge.artists.model.Album;
import com.zedge.artists.model._data.DummyData;
import com.zedge.artists.web.dto.AlbumDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlbumDtoMapperTest {

    @Test
    void fromAlbum_shouldMapAllRequiredValues() {
        Album album = DummyData.baseAlbum().build();

        AlbumDto result = AlbumDtoMapper.fromAlbum(album);

        assertEquals(album.getId(), result.getId());
        assertEquals(album.getPlatformId(), result.getPlatformId());
        assertEquals(album.getTitle(), result.getTitle());
        assertEquals(album.getReleaseDate(), result.getReleaseDate());
        assertEquals(album.getTrackCount(), result.getTrackCount());
    }
}