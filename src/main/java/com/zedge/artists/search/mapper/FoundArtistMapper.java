package com.zedge.artists.search.mapper;

import com.zedge.artists.model.Artist;
import com.zedge.artists.search.dto.FoundArtist;

public class FoundArtistMapper {

    public static Artist toArtist(FoundArtist artist) {
        return Artist.builder()
                .platformId(artist.getPlatformId())
                .amgId(artist.getAmgId())
                .name(artist.getName())
                .primaryGenreName(artist.getPrimaryGenreName())
                .build();
    }
}
