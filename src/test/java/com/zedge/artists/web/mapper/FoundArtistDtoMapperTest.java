package com.zedge.artists.web.mapper;

import com.zedge.artists.search.dto.FoundArtist;
import com.zedge.artists.search.dto._data.SearchDummyData;
import com.zedge.artists.web.dto.FoundArtistDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FoundArtistDtoMapperTest {

    @Test
    void fromFoundArtist_shouldMapAllRequiredValues() {
        FoundArtist foundArtist = SearchDummyData.baseFoundArtist().build();

        FoundArtistDto result = FoundArtistDtoMapper.fromFoundArtist(foundArtist);

        assertEquals(foundArtist.getName(), result.getName());
        assertEquals(foundArtist.getPlatformId(), result.getPlatformId());
        assertEquals(foundArtist.getAmgId(), result.getAmgId());
        assertEquals(foundArtist.getPlatformId(), result.getPlatformId());
        assertEquals(foundArtist.getPrimaryGenreName(), result.getPrimaryGenreName());
    }
}