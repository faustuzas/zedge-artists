package com.zedge.artists.search.itunes.mapper;

import com.zedge.artists.search.itunes.dto.ItunesArtist;
import com.zedge.artists.search.dto.FoundArtist;

public class ItunesArtistMapper {

    public static FoundArtist toFoundArtist(ItunesArtist artist) {
        return FoundArtist.builder()
                .name(artist.getArtistName())
                .amgId(artist.getAmgArtistId())
                .platformId(artist.getArtistId())
                .primaryGenreName(artist.getPrimaryGenreName())
                .build();
    };
}
