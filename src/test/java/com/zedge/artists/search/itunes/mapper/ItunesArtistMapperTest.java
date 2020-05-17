package com.zedge.artists.search.itunes.mapper;

import com.zedge.artists.search.dto.FoundArtist;
import com.zedge.artists.search.itunes.dto._data.ItunesDummyData;
import com.zedge.artists.search.itunes.dto.ItunesArtist;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItunesArtistMapperTest {

    @Test
    void toArtist_shouldMapAllRequiredValues() {
        ItunesArtist itunesArtist = ItunesDummyData.itunesArtistBase()
                .build();

        FoundArtist artist = ItunesArtistMapper.toFoundArtist(itunesArtist);

        assertEquals(itunesArtist.getArtistId(), artist.getPlatformId());
        assertEquals(itunesArtist.getAmgArtistId(), artist.getAmgId());
        assertEquals(itunesArtist.getArtistName(), artist.getName());
        assertEquals(itunesArtist.getPrimaryGenreName(), artist.getPrimaryGenreName());
    }
}