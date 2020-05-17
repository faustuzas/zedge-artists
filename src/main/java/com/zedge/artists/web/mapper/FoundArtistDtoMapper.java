package com.zedge.artists.web.mapper;

import com.zedge.artists.search.dto.FoundArtist;
import com.zedge.artists.web.dto.FoundArtistDto;

public class FoundArtistDtoMapper {

    public static FoundArtistDto fromFoundArtist(FoundArtist artist) {
        return FoundArtistDto.builder()
                .name(artist.getName())
                .amgId(artist.getAmgId())
                .platformId(artist.getPlatformId())
                .primaryGenreName(artist.getPrimaryGenreName())
                .build();
    }
}
